package echo.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A single to-do item tracked by Echo. Holds a description, a task type
 * (TODO/DEADLINE/EVENT) and whether the task has been marked as done.
 */
public class Task {
    public static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final String description;
    private final TaskType type;
    private boolean isDone;

    public Task(String description, TaskType type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[" + type.tag() + "][" + getStatusIcon() + "] " + description;
    }

    /**
     * Renders this task as a single line for the save file, e.g.
     * {@code T | 1 | read book}. Subclasses append their own extra fields
     * after calling this via super, mirroring the toString() pattern.
     */
    public String toFileFormat() {
        return type.tag() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns whether this task occurs on the given date. A plain Task
     * (Todo) is never date-specific; Deadline/Event override this.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }
}
