package echo.task;

import java.util.List;

/**
 * Wraps the in-memory list of tasks and the membership operations Echo
 * performs on it (add/remove/get/size). Marking a task done/not-done stays
 * that Task's own responsibility (Task#markAsDone/markAsNotDone) rather
 * than living here, since that's a property of one task, not the list.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Wraps the given list of tasks.
     *
     * @param tasks The initial tasks (e.g. freshly loaded from Storage).
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the task at the given zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Returns the task at the given zero-based index. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the number of tasks currently in the list. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list, for callers (Ui rendering, Storage
     * saving) that need to read or persist every task.
     */
    public List<Task> getAll() {
        return tasks;
    }
}
