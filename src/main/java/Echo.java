import java.time.LocalDate;
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
                String cmd = Parser.getCommandWord(input);
                String rest = Parser.getArguments(input);

                switch (cmd) {
                    case "list":
                        ui.showList(tasks.getAll());
                        break;
                    case "mark": {
                        int idx = Parser.parseTaskIndex(rest, tasks.size());
                        tasks.get(idx).markAsDone();
                        ui.showTaskMarked(tasks.get(idx));
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "unmark": {
                        int idx = Parser.parseTaskIndex(rest, tasks.size());
                        tasks.get(idx).markAsNotDone();
                        ui.showTaskUnmarked(tasks.get(idx));
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "delete": {
                        int idx = Parser.parseTaskIndex(rest, tasks.size());
                        Task removed = tasks.remove(idx);
                        ui.showTaskRemoved(removed, tasks.size());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "todo": {
                        Task t = Parser.parseTodo(rest);
                        tasks.add(t);
                        ui.showTaskAdded(t, tasks.size());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "deadline": {
                        Task t = Parser.parseDeadline(rest);
                        tasks.add(t);
                        ui.showTaskAdded(t, tasks.size());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "event": {
                        Task t = Parser.parseEvent(rest);
                        tasks.add(t);
                        ui.showTaskAdded(t, tasks.size());
                        storage.save(tasks.getAll());
                        break;
                    }
                    case "on": {
                        LocalDate date = Parser.parseOnDate(rest);
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
}
