import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Echo {
    private static final String LINE =
            "____________________________________________________________";

    public static void main(String[] args) {
        String banner = " _____      _           \n"
                + "| ____|___ | |__   ___  \n"
                + "|  _| / __|| '_ \\ / _ \\ \n"
                + "| |__| (__ | | | | (_) |\n"
                + "|_____\\___||_| |_|\\___/ \n";

        System.out.println(LINE);
        System.out.print(banner);
        System.out.println("Hello! I'm Echo.");
        System.out.println("What's on your mind?");
        System.out.println(LINE);

        // Read-then-decide loop: reads a line, then checks for the exit word.
        // Using `while (true) + break` keeps the "read, then decide" order obvious
        // and avoids the priming/re-read pair a condition-driven loop would need.
        Scanner sc = new Scanner(System.in);

        Storage storage = new Storage("data/echo.txt");
        List<Task> items = storage.load();

        while (true) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                break;
            }
            System.out.println(LINE);
            try {
                // Split once so cmd is the first word and rest is everything after
                // the first space (empty string if there is no space).
                String[] parts = input.split(" ", 2);
                String cmd = parts[0];
                String rest = parts.length > 1 ? parts[1] : "";

                switch (cmd) {
                    case "list":
                        System.out.println("Here are the tasks in your list:");
                        for (int i = 0; i < items.size(); i++) {
                            System.out.println((i + 1) + "." + items.get(i));
                        }
                        break;
                    case "mark": {
                        int idx = parseTaskIndex(rest, items.size());
                        items.get(idx).markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + items.get(idx));
                        storage.save(items);
                        break;
                    }
                    case "unmark": {
                        int idx = parseTaskIndex(rest, items.size());
                        items.get(idx).markAsNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + items.get(idx));
                        storage.save(items);
                        break;
                    }
                    case "delete": {
                        int idx = parseTaskIndex(rest, items.size());
                        Task removed = items.remove(idx);
                        announceRemoved(removed, items.size());
                        storage.save(items);
                        break;
                    }
                    case "todo": {
                        String desc = rest.trim();
                        if (desc.isEmpty()) {
                            throw new EchoException("The description of a todo cannot be empty.");
                        }
                        Task t = new Todo(desc);
                        items.add(t);
                        announceAdded(t, items.size());
                        storage.save(items);
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
                        Task t = new Deadline(desc, by);
                        items.add(t);
                        announceAdded(t, items.size());
                        storage.save(items);
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
                        Task t = new Event(desc, from, to);
                        items.add(t);
                        announceAdded(t, items.size());
                        storage.save(items);
                        break;
                    }
                    default:
                        throw new EchoException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (EchoException e) {
                System.out.println("OOPS!!! " + e.getMessage());
            }
            System.out.println(LINE);
        }
        sc.close();

        System.out.println(LINE);
        System.out.println("Bye. Hope to echo with you again soon!");
        System.out.println(LINE);
    }

    private static void announceAdded(Task t, int newCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + newCount + " tasks in the list.");
    }

    private static void announceRemoved(Task t, int newCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + newCount + " tasks in the list.");
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
