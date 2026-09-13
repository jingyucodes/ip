package echo.task; // same package as the class being tested

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void clearAll_nonEmptyList_returnsRemovedTasksAndEmptiesList() {
        List<Task> initial = new ArrayList<>();
        initial.add(new Todo("read book"));
        initial.add(new Todo("write essay"));
        TaskList taskList = new TaskList(initial);

        List<Task> removed = taskList.clearAll();

        assertEquals(2, removed.size());
        assertEquals("[T][ ] read book", removed.get(0).toString());
        assertEquals("[T][ ] write essay", removed.get(1).toString());
        assertEquals(0, taskList.size());
    }

    @Test
    public void clearAll_emptyList_returnsEmptyListAndStaysEmpty() {
        TaskList taskList = new TaskList(new ArrayList<>());

        List<Task> removed = taskList.clearAll();

        assertTrue(removed.isEmpty());
        assertEquals(0, taskList.size());
    }
}
