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
            } else {
                items[count] = new Task(input);
                count++;
                System.out.println("added: " + input);
            }
            System.out.println(LINE);
        }
        sc.close();

        System.out.println(LINE);
        System.out.println("Bye. Hope to echo with you again soon!");
        System.out.println(LINE);
    }
}
