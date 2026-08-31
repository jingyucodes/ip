import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Echo {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage("data/echo.txt");
        TaskList tasks = new TaskList(storage.load());

        // Read-then-decide loop: reads a line, then checks for the exit word.
        // Using `while (true) + break` keeps the "read, then decide" order obvious
        // and avoids the priming/re-read pair a condition-driven loop would need.
        while (true) {
            String input = ui.readCommand();
            if (input.equals("bye")) {
                break;
            }
            ui.showLine();
            try {
                // Split once so cmd is the first word and rest is everything after
                // the first space (empty string if there is no space).
                String[] parts = input.split(" ", 2);
                String cmd = parts[0];
                String rest = parts.length > 1 ? parts[1] : "";

                switch (cmd) {
                    case "list":
                        ui.showList(tasks.getAll());
                        break;
                    case "mark": {
                        int idx = parseTaskIndex(rest, tasks.size());
                        tasks.get(idx).markAsDone();
                        ui.showTaskMarked(tasks.get(idx));
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "unmark": {
                        int idx = parseTaskIndex(rest, tasks.size());
                        tasks.get(idx).markAsNotDone();
                        ui.showTaskUnmarked(tasks.get(idx));
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "delete": {
                        int idx = parseTaskIndex(rest, tasks.size());
                        Task removed = tasks.remove(idx);
                        ui.showTaskRemoved(removed, tasks.size());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "todo": {
                        String desc = rest.trim();
                        if (desc.isEmpty()) {
                            throw new EchoException("The description of a todo cannot be empty.");
                        }
                        Task t = new Todo(desc);
                        tasks.add(t);
                        ui.showTaskAdded(t, tasks.size());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "deadline": {
                        // split with limit 2 so a description containing "/by" isn't broken up
                        String[] p = rest.split(" /by ", 2);
                        if (p.length < 2) {
                            throw new EchoException("A deadline needs a '/by <when>' clause. "
                                    + "Example: deadline return book /by Sunday");
                        }
                        String desc = p[0].trim();
                        String by = p[1].trim();
                        if (desc.isEmpty()) {
                            throw new EchoException("The description of a deadline cannot be empty.");
                        }
                        if (by.isEmpty()) {
                            throw new EchoException("The '/by' time of a deadline cannot be empty.");
                        }
                        LocalDate byDate = parseDate(by,
                                "Invalid date for '/by'. Use yyyy-mm-dd, e.g. 2019-10-15.");
                        Task t = new Deadline(desc, byDate);
                        tasks.add(t);
                        ui.showTaskAdded(t, tasks.size());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "event": {
                        String[] p1 = rest.split(" /from ", 2);
                        if (p1.length < 2) {
                            throw new EchoException("An event needs a '/from <when>' clause. "
                                    + "Example: event meeting /from Mon 2pm /to 4pm");
                        }
                        String[] p2 = p1[1].split(" /to ", 2);
                        if (p2.length < 2) {
                            throw new EchoException("An event needs a '/to <when>' clause after '/from'.");
                        }
                        String desc = p1[0].trim();
                        String from = p2[0].trim();
                        String to = p2[1].trim();
                        if (desc.isEmpty()) {
                            throw new EchoException("The description of an event cannot be empty.");
                        }
                        if (from.isEmpty()) {
                            throw new EchoException("The '/from' time cannot be empty.");
                        }
                        if (to.isEmpty()) {
                            throw new EchoException("The '/to' time cannot be empty.");
                        }
                        LocalDate fromDate = parseDate(from,
                                "Invalid date for '/from'. Use yyyy-mm-dd, e.g. 2019-10-15.");
                        LocalDate toDate = parseDate(to,
                                "Invalid date for '/to'. Use yyyy-mm-dd, e.g. 2019-10-15.");
                        if (toDate.isBefore(fromDate)) {
                            throw new EchoException("An event's '/to' date cannot be before its '/from' date.");
                        }
                        Task t = new Event(desc, fromDate, toDate);
                        tasks.add(t);
                        ui.showTaskAdded(t, tasks.size());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "on": {
                        String dateStr = rest.trim();
                        if (dateStr.isEmpty()) {
                            throw new EchoException("Please give a date. Example: on 2019-10-15");
                        }
                        LocalDate date = parseDate(dateStr, "Invalid date. Use yyyy-mm-dd, e.g. 2019-10-15.");
                        List<Task> matches = new ArrayList<>();
                        for (Task task : tasks.getAll()) {
                            if (task.occursOn(date)) {
                                matches.add(task);
                            }
                        }
                        ui.showTasksOnDate(date, matches);
                        break;
                    }
                    default:
                        throw new EchoException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (EchoException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
        ui.close();
        ui.showGoodbye();
    }

    private static LocalDate parseDate(String s, String errorMessage) throws EchoException {
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            throw new EchoException(errorMessage);
        }
    }

    private static int parseTaskIndex(String rest, int count) throws EchoException {
        String s = rest.trim();
        if (s.isEmpty()) {
            throw new EchoException("Please give a task number. Example: mark 2");
        }
        int n;
        try {
            n = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new EchoException("Task number must be an integer, not '" + s + "'.");
        }
        if (n < 1 || n > count) {
            throw new EchoException("Task " + n + " does not exist. You have " + count + " task(s).");
        }
        return n - 1;
    }
}
