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

        // Fixed-size store per spec (<=100 items). ArrayList would grow
        // automatically, but the array + count pair makes size bookkeeping explicit.
        Task[] items = new Task[100];
        int count = 0;

        while (true) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                break;
            }
            System.out.println(LINE);
            if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + "." + items[i]);
                }
            } else if (input.startsWith("mark ")) {
                int idx = Integer.parseInt(input.substring(5)) - 1;
                items[idx].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + items[idx]);
            } else if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7)) - 1;
                items[idx].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + items[idx]);
            } else if (input.startsWith("todo ")) {
                Task t = new Todo(input.substring(5));
                items[count++] = t;
                announceAdded(t, count);
            } else if (input.startsWith("deadline ")) {
                // split with limit 2 so a description containing "/by" isn't broken up
                String[] p = input.substring(9).split(" /by ", 2);
                Task t = new Deadline(p[0], p[1]);
                items[count++] = t;
                announceAdded(t, count);
            } else if (input.startsWith("event ")) {
                String[] p1 = input.substring(6).split(" /from ", 2);
                String[] p2 = p1[1].split(" /to ", 2);
                Task t = new Event(p1[0], p2[0], p2[1]);
                items[count++] = t;
                announceAdded(t, count);
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
}
