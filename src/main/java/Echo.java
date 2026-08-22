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
        while (true) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                break;
            }
            System.out.println(LINE);
            System.out.println(input);
            System.out.println(LINE);
        }
        sc.close();

        System.out.println(LINE);
        System.out.println("Bye. Hope to echo with you again soon!");
        System.out.println(LINE);
    }
}
