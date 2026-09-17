package echo.task; // same package as the class being tested

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void add_task_increasesSizeAndAppendsToEnd() {
        TaskList taskList = new TaskList(new ArrayList<>());

        taskList.add(new Todo("read book"));

        assertEquals(1, taskList.size());
        assertEquals("[T][ ] read book", taskList.get(0).toString());
    }

    @Test
    public void get_validIndex_returnsThatTask() {
        List<Task> initial = new ArrayList<>();
        initial.add(new Todo("read book"));
        initial.add(new Todo("write essay"));
        TaskList taskList = new TaskList(initial);

        assertEquals("[T][ ] write essay", taskList.get(1).toString());
    }

    @Test
    public void remove_validIndex_removesAndReturnsThatTask() {
        List<Task> initial = new ArrayList<>();
        initial.add(new Todo("read book"));
        initial.add(new Todo("write essay"));
        TaskList taskList = new TaskList(initial);

        Task removed = taskList.remove(0);

        assertEquals("[T][ ] read book", removed.toString());
        assertEquals(1, taskList.size());
        assertEquals("[T][ ] write essay", taskList.get(0).toString());
    }

    @Test
    public void size_reflectsNumberOfTasksAdded() {
        TaskList taskList = new TaskList(new ArrayList<>());
        assertEquals(0, taskList.size());

        taskList.add(new Todo("read book"));
        taskList.add(new Todo("write essay"));

        assertEquals(2, taskList.size());
    }

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
