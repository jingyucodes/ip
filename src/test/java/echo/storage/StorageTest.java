package echo.storage; // same package as the class being tested

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import echo.task.Task;
import echo.task.Todo;

public class StorageTest {
    @TempDir
    Path tempDir;

    @Test
    public void archive_nonEmptyList_appendsToArchiveFile() throws IOException {
        Storage storage = new Storage(tempDir.resolve("echo.txt").toString());
        List<Task> tasks = List.of(new Todo("read book"), new Todo("write essay"));

        storage.archive(tasks);

        Path archiveFile = tempDir.resolve("archive.txt");
        List<String> lines = Files.readAllLines(archiveFile);
        assertEquals(List.of("T | 0 | read book", "T | 0 | write essay"), lines);
    }

    @Test
    public void archive_calledTwice_accumulatesAcrossCalls() throws IOException {
        Storage storage = new Storage(tempDir.resolve("echo.txt").toString());

        storage.archive(List.of(new Todo("first task")));
        storage.archive(List.of(new Todo("second task")));

        Path archiveFile = tempDir.resolve("archive.txt");
        List<String> lines = Files.readAllLines(archiveFile);
        assertEquals(List.of("T | 0 | first task", "T | 0 | second task"), lines);
    }
}
