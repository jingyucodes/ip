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

    /** Creates a Ui that reads commands from standard input. */
    public Ui() {
        this.sc = new Scanner(System.in);
    }

    /** Prints the divider line used to separate sections of output. */
    public void showLine() {
        System.out.println(LINE);
    }

    /** Prints the startup banner and greeting. */
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

    /** Prints the farewell message shown when the user exits. */
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

    /** Closes the underlying input scanner. */
    public void close() {
        sc.close();
    }

    /** Prints an error message, prefixed to stand out as a failure. */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    /** Prints every task in the given list, numbered from 1. */
    public void showList(List<Task> items) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + "." + items.get(i));
        }
    }

    /**
     * Prints confirmation that a task was added, plus the new task count.
     *
     * @param t The task that was added.
     * @param newCount The list's size after adding it.
     */
    public void showTaskAdded(Task t, int newCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + newCount + " tasks in the list.");
    }

    /**
     * Prints confirmation that a task was removed, plus the new task
     * count.
     *
     * @param t The task that was removed.
     * @param newCount The list's size after removing it.
     */
    public void showTaskRemoved(Task t, int newCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + newCount + " tasks in the list.");
    }

    /** Prints confirmation that a task was marked as done. */
    public void showTaskMarked(Task t) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + t);
    }

    /** Prints confirmation that a task was marked as not done. */
    public void showTaskUnmarked(Task t) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + t);
    }

    /**
     * Prints every task in the given list as occurring on the given
     * date, numbered from 1, or a "(none)" fallback if the list is
     * empty.
     *
     * @param date The date being queried (used only for the header text).
     * @param matches Tasks that occur on that date.
     */
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

    /**
     * Prints every task in the given list as a keyword match, numbered
     * from 1, or a "(none)" fallback if the list is empty.
     */
    public void showMatchingTasks(List<Task> matches) {
        System.out.println("Here are the matching tasks in your list:");
        if (matches.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + "." + matches.get(i));
        }
    }
}
