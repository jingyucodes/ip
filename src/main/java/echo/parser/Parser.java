package echo.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import echo.EchoException;
import echo.task.Deadline;
import echo.task.Event;
import echo.task.Task;
import echo.task.Todo;

/**
 * Makes sense of a raw user command line: splitting it into a command word
 * and arguments, and turning each command's arguments into the Task (or
 * other value) Echo needs to act on. Throws EchoException with a
 * user-facing message on anything malformed, exactly as Echo's inline
 * parsing used to.
 */
public class Parser {
    private Parser() {
    }

    /**
     * Returns the first word of the input line (the command).
     */
    public static String getCommandWord(String input) {
        return input.split(" ", 2)[0];
    }

    /**
     * Returns everything after the first word (empty string if there is
     * no space, i.e. the command has no arguments).
     */
    public static String getArguments(String input) {
        String[] parts = input.split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    public static Task parseTodo(String rest) throws EchoException {
        String desc = rest.trim();
        if (desc.isEmpty()) {
            throw new EchoException("The description of a todo cannot be empty.");
        }
        return new Todo(desc);
    }

    public static Task parseDeadline(String rest) throws EchoException {
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
        LocalDate byDate = parseDate(by, "Invalid date for '/by'. Use yyyy-mm-dd, e.g. 2019-10-15.");
        return new Deadline(desc, byDate);
    }

    public static Task parseEvent(String rest) throws EchoException {
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
        LocalDate fromDate = parseDate(from, "Invalid date for '/from'. Use yyyy-mm-dd, e.g. 2019-10-15.");
        LocalDate toDate = parseDate(to, "Invalid date for '/to'. Use yyyy-mm-dd, e.g. 2019-10-15.");
        if (toDate.isBefore(fromDate)) {
            throw new EchoException("An event's '/to' date cannot be before its '/from' date.");
        }
        return new Event(desc, fromDate, toDate);
    }

    public static LocalDate parseOnDate(String rest) throws EchoException {
        String dateStr = rest.trim();
        if (dateStr.isEmpty()) {
            throw new EchoException("Please give a date. Example: on 2019-10-15");
        }
        return parseDate(dateStr, "Invalid date. Use yyyy-mm-dd, e.g. 2019-10-15.");
    }

    public static int parseTaskIndex(String rest, int count) throws EchoException {
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

    private static LocalDate parseDate(String s, String errorMessage) throws EchoException {
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            throw new EchoException(errorMessage);
        }
    }
}
