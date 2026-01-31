package haru;

import java.io.IOException;

/**
 * Main class for the Haru task management application.
 * Coordinates between UI, storage, and task list components.
 */
public class Haru {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a Haru application instance.
     *
     * @param filePath The file path for storing and loading tasks.
     */
    public Haru(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main application loop.
     * Displays welcome message, reads and processes user commands until exit.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String input = ui.readCommand();
                Parser.Command cmd = Parser.parseCommand(input);

                switch (cmd) {
                case BYE:
                    ui.showGoodbye();
                    ui.close();
                    return;

                case LIST:
                    ui.showTaskList(tasks.getTasks());
                    break;

                case MARK:
                    int mIdx = Parser.parseTaskIndex(input, 5, tasks.size());
                    tasks.get(mIdx).markAsDone();
                    storage.save(tasks);
                    ui.showTaskMarked(tasks.get(mIdx));
                    break;

                case UNMARK:
                    int uIdx = Parser.parseTaskIndex(input, 7, tasks.size());
                    tasks.get(uIdx).markAsNotDone();
                    storage.save(tasks);
                    ui.showTaskUnmarked(tasks.get(uIdx));
                    break;

                case DELETE:
                    int dIdx = Parser.parseTaskIndex(input, 7, tasks.size());
                    Task removedTask = tasks.remove(dIdx);
                    storage.save(tasks);
                    ui.showTaskDeleted(removedTask, tasks.size());
                    break;

                case TODO:
                    Task todo = Parser.parseTodo(input);
                    tasks.add(todo);
                    storage.save(tasks);
                    ui.showTaskAdded(todo, tasks.size());
                    break;

                case DEADLINE:
                    Task deadline = Parser.parseDeadline(input);
                    tasks.add(deadline);
                    storage.save(tasks);
                    ui.showTaskAdded(deadline, tasks.size());
                    break;

                case EVENT:
                    Task event = Parser.parseEvent(input);
                    tasks.add(event);
                    storage.save(tasks);
                    ui.showTaskAdded(event, tasks.size());
                    break;

                default:
                    throw new HaruException("I'm sorry, but I don't know what that means :-(");
                }
            } catch (HaruException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Please use a valid number.");
            } catch (IOException e) {
                ui.showError("I couldn't save tasks to disk.");
            }
        }
    }

    /**
     * Main entry point of the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Haru("data/haru.txt").run();
    }
}
