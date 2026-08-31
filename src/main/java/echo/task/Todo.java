package echo.task;

/**
 * A task with no date/time attached. Its type tag "[T]" comes from
 * TaskType.TODO passed to the Task superclass.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
