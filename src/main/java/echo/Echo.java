package echo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import echo.parser.Parser;
import echo.storage.Storage;
import echo.task.Task;
import echo.task.TaskList;
import echo.ui.Ui;

/**
 * Entry point and orchestrator for the Echo chatbot. Wires together Ui,
 * Storage, and TaskList, then runs the read-command/dispatch/respond loop.
 */
public class Echo {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates an Echo bound to the given save-file path, loading any
     * previously saved tasks immediately.
     *
     * @param filePath Relative path to the save file, e.g. "data/echo.txt".
     */
    public Echo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Runs the read-then-decide loop: reads a line, then checks for the
     * exit word. Using `while (true) + break` keeps the "read, then
     * decide" order obvious and avoids the priming/re-read pair a
     * condition-driven loop would need.
     */
    public void run() {
        ui.showWelcome();
        while (true) {
            String input = ui.readCommand();
            if (input.equals("bye")) {
                break;
            }
            ui.showLine();
            try {
                String cmd = Parser.getCommandWord(input);
                String rest = Parser.getArguments(input);
                executeCommand(cmd, rest);
            } catch (EchoException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
        ui.close();
        ui.showGoodbye();
    }

    /**
     * Executes one already-split command word and argument string against
     * the current task list, showing the result via Ui and persisting any
     * change via Storage. Pulled out of run()'s loop so a future caller
     * (e.g. a GUI controller) can dispatch a single command the same way
     * the console loop does, without duplicating this switch.
     *
     * @param cmd Command word, e.g. "todo" or "mark".
     * @param rest Text after the command word.
     * @throws EchoException If the command is unrecognised or its
     *     arguments are invalid.
     */
    private void executeCommand(String cmd, String rest) throws EchoException {
        switch (cmd) {
            case "list":
                ui.showList(tasks.getAll());
                break;
            case "mark": {
                int idx = Parser.parseTaskIndex(rest, tasks.size());
                tasks.get(idx).markAsDone();
                ui.showTaskMarked(tasks.get(idx));
                storage.save(tasks.getAll());
                break;
            }
            case "unmark": {
                int idx = Parser.parseTaskIndex(rest, tasks.size());
                tasks.get(idx).markAsNotDone();
                ui.showTaskUnmarked(tasks.get(idx));
                storage.save(tasks.getAll());
                break;
            }
            case "delete": {
                int idx = Parser.parseTaskIndex(rest, tasks.size());
                Task removed = tasks.remove(idx);
                ui.showTaskRemoved(removed, tasks.size());
                storage.save(tasks.getAll());
                break;
            }
            case "todo": {
                Task t = Parser.parseTodo(rest);
                tasks.add(t);
                ui.showTaskAdded(t, tasks.size());
                storage.save(tasks.getAll());
                break;
            }
            case "deadline": {
                Task t = Parser.parseDeadline(rest);
                tasks.add(t);
                ui.showTaskAdded(t, tasks.size());
                storage.save(tasks.getAll());
                break;
            }
            case "event": {
                Task t = Parser.parseEvent(rest);
                tasks.add(t);
                ui.showTaskAdded(t, tasks.size());
                storage.save(tasks.getAll());
                break;
            }
            case "on": {
                LocalDate date = Parser.parseOnDate(rest);
                List<Task> matches = new ArrayList<>();
                for (Task task : tasks.getAll()) {
                    if (task.occursOn(date)) {
                        matches.add(task);
                    }
                }
                ui.showTasksOnDate(date, matches);
                break;
            }
            case "find": {
                String keyword = Parser.parseFindKeyword(rest);
                List<Task> matches = new ArrayList<>();
                for (Task task : tasks.getAll()) {
                    if (task.matchesKeyword(keyword)) {
                        matches.add(task);
                    }
                }
                ui.showMatchingTasks(matches);
                break;
            }
            default:
                throw new EchoException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Starts Echo with the default save-file path.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Echo("data/echo.txt").run();
    }
}
