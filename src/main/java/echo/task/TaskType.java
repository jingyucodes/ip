package echo.task;

/**
 * The kind of a Task. Each value carries the single-letter tag used when the
 * task is rendered (e.g., "[T]", "[D]", "[E]").
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String tag;

    TaskType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
