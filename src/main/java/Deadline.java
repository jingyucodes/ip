/**
 * A task that must be completed before a given date/time. The "by" value is
 * kept as a plain String at this level; a later increment may parse it.
 */
public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
