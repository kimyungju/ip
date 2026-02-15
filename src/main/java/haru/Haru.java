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
                    int mIdx = Parser.parseTaskIndex(input, Parser.MARK_PREFIX_LENGTH, tasks.size());
                    tasks.get(mIdx).markAsDone();
                    storage.save(tasks);
                    ui.showTaskMarked(tasks.get(mIdx));
                    break;

                case UNMARK:
                    int uIdx = Parser.parseTaskIndex(input, Parser.UNMARK_PREFIX_LENGTH, tasks.size());
                    tasks.get(uIdx).markAsNotDone();
                    storage.save(tasks);
                    ui.showTaskUnmarked(tasks.get(uIdx));
                    break;

                case DELETE:
                    int dIdx = Parser.parseTaskIndex(input, Parser.DELETE_PREFIX_LENGTH, tasks.size());
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

                case FIND:
                    if (input.length() <= Parser.FIND_PREFIX_LENGTH) {
                        throw new HaruException("Please provide a keyword to search for.");
                    }
                    String keyword = input.substring(Parser.FIND_PREFIX_LENGTH).trim();
                    TaskList matchingTasks = tasks.findTasks(keyword);
                    ui.showMatchingTasks(matchingTasks.getTasks());
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
     * Generates a response for the user's chat message.
     * Used by the GUI to get Haru's response without printing to console.
     *
     * @param input The user input string.
     * @return Haru's response, or null if the application should exit (bye command).
     */
    public String getResponse(String input) {
        StringBuilder sb = new StringBuilder();
        String line = "    ____________________________________________________________";

        try {
            Parser.Command cmd = Parser.parseCommand(input);

            switch (cmd) {
            case BYE:
                sb.append(line).append("\n");
                sb.append("     Bye. Hope to see you again soon!\n");
                sb.append(line);
                return sb.toString();

            case LIST:
                sb.append(line).append("\n");
                sb.append("     Here are the tasks in your list:\n");
                for (int i = 0; i < tasks.size(); i++) {
                    sb.append("     ").append(i + 1).append(".").append(tasks.get(i)).append("\n");
                }
                sb.append(line);
                return sb.toString();

            case MARK:
                int mIdx = Parser.parseTaskIndex(input, Parser.MARK_PREFIX_LENGTH, tasks.size());
                tasks.get(mIdx).markAsDone();
                storage.save(tasks);
                sb.append(line).append("\n");
                sb.append("     Nice! I've marked this task as done:\n       ").append(tasks.get(mIdx)).append("\n");
                sb.append(line);
                return sb.toString();

            case UNMARK:
                int uIdx = Parser.parseTaskIndex(input, Parser.UNMARK_PREFIX_LENGTH, tasks.size());
                tasks.get(uIdx).markAsNotDone();
                storage.save(tasks);
                sb.append(line).append("\n");
                sb.append("     OK, I've marked this task as not done yet:\n       ").append(tasks.get(uIdx)).append("\n");
                sb.append(line);
                return sb.toString();

            case DELETE:
                int dIdx = Parser.parseTaskIndex(input, Parser.DELETE_PREFIX_LENGTH, tasks.size());
                Task removedTask = tasks.remove(dIdx);
                storage.save(tasks);
                sb.append(line).append("\n");
                sb.append("     Noted. I've removed this task:\n       ").append(removedTask).append("\n");
                sb.append("     Now you have ").append(tasks.size()).append(" tasks in the list.\n");
                sb.append(line);
                return sb.toString();

            case TODO:
                Task todo = Parser.parseTodo(input);
                tasks.add(todo);
                storage.save(tasks);
                sb.append(line).append("\n");
                sb.append("     Got it. I've added this task:\n       ").append(todo).append("\n");
                sb.append("     Now you have ").append(tasks.size()).append(" tasks in the list.\n");
                sb.append(line);
                return sb.toString();

            case DEADLINE:
                Task deadline = Parser.parseDeadline(input);
                tasks.add(deadline);
                storage.save(tasks);
                sb.append(line).append("\n");
                sb.append("     Got it. I've added this task:\n       ").append(deadline).append("\n");
                sb.append("     Now you have ").append(tasks.size()).append(" tasks in the list.\n");
                sb.append(line);
                return sb.toString();

            case EVENT:
                Task event = Parser.parseEvent(input);
                tasks.add(event);
                storage.save(tasks);
                sb.append(line).append("\n");
                sb.append("     Got it. I've added this task:\n       ").append(event).append("\n");
                sb.append("     Now you have ").append(tasks.size()).append(" tasks in the list.\n");
                sb.append(line);
                return sb.toString();

            case FIND:
                if (input.length() <= Parser.FIND_PREFIX_LENGTH) {
                    throw new HaruException("Please provide a keyword to search for.");
                }
                String keyword = input.substring(Parser.FIND_PREFIX_LENGTH).trim();
                TaskList matchingTasks = tasks.findTasks(keyword);
                sb.append(line).append("\n");
                if (matchingTasks.size() == 0) {
                    sb.append("     No matching tasks found.\n");
                } else {
                    sb.append("     Here are the matching tasks in your list:\n");
                    for (int i = 0; i < matchingTasks.size(); i++) {
                        sb.append("     ").append(i + 1).append(".").append(matchingTasks.get(i)).append("\n");
                    }
                }
                sb.append(line);
                return sb.toString();

            default:
                throw new HaruException("I'm sorry, but I don't know what that means :-(");
            }
        } catch (HaruException e) {
            sb.append(line).append("\n");
            sb.append("     OOPS!!! ").append(e.getMessage()).append("\n");
            sb.append(line);
            return sb.toString();
        } catch (NumberFormatException e) {
            sb.append(line).append("\n");
            sb.append("     OOPS!!! Please use a valid number.\n");
            sb.append(line);
            return sb.toString();
        } catch (IOException e) {
            sb.append(line).append("\n");
            sb.append("     OOPS!!! I couldn't save tasks to disk.\n");
            sb.append(line);
            return sb.toString();
        }
    }

    /**
     * Returns the welcome message for the GUI.
     *
     * @return The welcome message string.
     */
    public String getWelcomeMessage() {
        return "Hello! I'm Haru\n"
                + " _   _                 \n"
                + "| | | | __ _ _ __ _   _ \n"
                + "| |_| |/ _` | '__| | | |\n"
                + "|  _  | (_| | |   | |_| |\n"
                + "|_| |_|\\__,_|_|   \\__,_|\n"
                + "\nWhat can I do for you?";
    }

    /**
     * Main entry point of the application (for text-based UI).
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Haru("data/haru.txt").run();
    }
}
