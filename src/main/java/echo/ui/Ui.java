package echo.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import echo.task.Task;

/**
 * Handles all interaction with the user: reading raw command lines from
 * the console and printing every message Echo shows (banner, task
 * confirmations, errors, listings). Echo's main loop delegates all
 * console I/O here instead of calling System.out/Scanner directly.
 */
public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    private final Scanner sc;

    public Ui() {
        this.sc = new Scanner(System.in);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showWelcome() {
        String banner = " _____      _           \n"
                + "| ____|___ | |__   ___  \n"
                + "|  _| / __|| '_ \\ / _ \\ \n"
                + "| |__| (__ | | | | (_) |\n"
                + "|_____\\___||_| |_|\\___/ \n";

        showLine();
        System.out.print(banner);
        System.out.println("Hello! I'm Echo.");
        System.out.println("What's on your mind?");
        showLine();
    }

    public void showGoodbye() {
        showLine();
        System.out.println("Bye. Hope to echo with you again soon!");
        showLine();
    }

    /**
     * Reads one raw line of input from the user.
     */
    public String readCommand() {
        return sc.nextLine();
    }

    public void close() {
        sc.close();
    }

    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    public void showList(List<Task> items) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + "." + items.get(i));
        }
    }

    public void showTaskAdded(Task t, int newCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + newCount + " tasks in the list.");
    }

    public void showTaskRemoved(Task t, int newCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + newCount + " tasks in the list.");
    }

    public void showTaskMarked(Task t) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + t);
    }

    public void showTaskUnmarked(Task t) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + t);
    }

    public void showTasksOnDate(LocalDate date, List<Task> matches) {
        System.out.println("Here are the tasks occurring on "
                + date.format(Task.DISPLAY_DATE_FORMAT) + ":");
        if (matches.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + "." + matches.get(i));
        }
    }
}
