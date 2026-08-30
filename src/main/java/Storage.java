import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes the task list to a save file on disk. The file location
 * is fixed at construction time; every other method operates on that one
 * path. Load/save never throw: any IO problem is reported with a short
 * warning so a save-file issue never prevents the chatbot from starting or
 * running.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a Storage bound to the given save-file path.
     *
     * @param filePath Relative path to the save file, e.g. "data/echo.txt".
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads tasks from the save file, creating the file (and its parent
     * folder) if either is missing. Lines that cannot be parsed are skipped
     * with a warning rather than aborting the whole load.
     */
    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        try {
            ensureFileExists();
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }
                Task task = parseLine(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load saved tasks (" + e.getMessage()
                    + "). Starting with an empty list.");
        }
        return tasks;
    }

    /**
     * Overwrites the save file with the given tasks, one per line.
     */
    public void save(List<Task> tasks) {
        try {
            ensureFileExists();
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(task.toFileFormat());
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Warning: could not save tasks (" + e.getMessage() + ").");
        }
    }

    private void ensureFileExists() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
        }
    }

    /**
     * Parses one save-file line into a Task, or returns null (with a
     * printed warning) if the line is corrupted/unrecognized.
     */
    private Task parseLine(String line) {
        try {
            String[] parts = line.split(" \\| ");
            String typeTag = parts[0].trim();
            boolean isDone = parts[1].trim().equals("1");
            String description = parts[2].trim();

            Task task = switch (typeTag) {
                case "T" -> new Todo(description);
                case "D" -> new Deadline(description, parts[3].trim());
                case "E" -> new Event(description, parts[3].trim(), parts[4].trim());
                default -> throw new IllegalArgumentException("Unknown task type: " + typeTag);
            };
            if (isDone) {
                task.markAsDone();
            }
            return task;
        } catch (RuntimeException e) {
            System.out.println("Warning: skipping corrupted line in save file: " + line);
            return null;
        }
    }
}
