package echo; // same package as the class being tested

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class EchoTest {
    @TempDir
    Path tempDir;

    private Echo newEcho() {
        return new Echo(tempDir.resolve("echo.txt").toString());
    }

    @Test
    public void getResponse_todo_addsTaskAndConfirms() {
        Echo echo = newEcho();

        String response = echo.getResponse("todo read book");

        assertEquals("Got it. I've added this task:\n"
                + "  [T][ ] read book\n"
                + "Now you have 1 tasks in the list.", response);
    }

    @Test
    public void getResponse_deadline_addsTaskAndConfirms() {
        Echo echo = newEcho();

        String response = echo.getResponse("deadline return book /by 2019-06-06");

        assertEquals("Got it. I've added this task:\n"
                + "  [D][ ] return book (by: Jun 06 2019)\n"
                + "Now you have 1 tasks in the list.", response);
    }

    @Test
    public void getResponse_event_addsTaskAndConfirms() {
        Echo echo = newEcho();

        String response = echo.getResponse("event trip /from 2019-08-01 /to 2019-08-03");

        assertEquals("Got it. I've added this task:\n"
                + "  [E][ ] trip (from: Aug 01 2019 to: Aug 03 2019)\n"
                + "Now you have 1 tasks in the list.", response);
    }

    @Test
    public void getResponse_list_showsAllAddedTasksInOrder() {
        Echo echo = newEcho();
        echo.getResponse("todo read book");
        echo.getResponse("todo write essay");

        String response = echo.getResponse("list");

        assertEquals("Here are the tasks in your list:\n"
                + "1.[T][ ] read book\n"
                + "2.[T][ ] write essay", response);
    }

    @Test
    public void getResponse_mark_marksThatTaskDone() {
        Echo echo = newEcho();
        echo.getResponse("todo read book");

        String response = echo.getResponse("mark 1");

        assertEquals("Nice! I've marked this task as done:\n"
                + "  [T][X] read book", response);
    }

    @Test
    public void getResponse_unmark_marksThatTaskNotDone() {
        Echo echo = newEcho();
        echo.getResponse("todo read book");
        echo.getResponse("mark 1");

        String response = echo.getResponse("unmark 1");

        assertEquals("OK, I've marked this task as not done yet:\n"
                + "  [T][ ] read book", response);
    }

    @Test
    public void getResponse_delete_removesThatTask() {
        Echo echo = newEcho();
        echo.getResponse("todo read book");
        echo.getResponse("todo write essay");

        String response = echo.getResponse("delete 1");

        assertEquals("Noted. I've removed this task:\n"
                + "  [T][ ] read book\n"
                + "Now you have 1 tasks in the list.", response);
    }

    @Test
    public void getResponse_on_showsTasksOccurringOnThatDate() {
        Echo echo = newEcho();
        echo.getResponse("deadline return book /by 2019-06-06");

        String response = echo.getResponse("on 2019-06-06");

        assertEquals("Here are the tasks occurring on Jun 06 2019:\n"
                + "1.[D][ ] return book (by: Jun 06 2019)", response);
    }

    @Test
    public void getResponse_archive_clearsListAndConfirmsCount() {
        Echo echo = newEcho();
        echo.getResponse("todo read book");

        String response = echo.getResponse("archive");

        assertEquals("Archived 1 task(s). Your list is now empty.", response);
        assertEquals("Here are the tasks in your list:", echo.getResponse("list"));
    }

    @Test
    public void getResponse_find_showsMatchingTasksOnly() {
        Echo echo = newEcho();
        echo.getResponse("todo read book");
        echo.getResponse("todo write essay");

        String response = echo.getResponse("find book");

        assertEquals("Here are the matching tasks in your list:\n"
                + "1.[T][ ] read book", response);
    }

    @Test
    public void getResponse_unrecognizedCommand_returnsErrorMessage() {
        Echo echo = newEcho();

        String response = echo.getResponse("blah");

        assertEquals("OOPS!!! I'm sorry, but I don't know what that means :-(", response);
    }

    @Test
    public void getResponse_bye_returnsGoodbyeMessageWithoutRunningACommand() {
        Echo echo = newEcho();

        String response = echo.getResponse("bye");

        assertEquals("Bye. Hope to echo with you again soon!", response);
    }
}
