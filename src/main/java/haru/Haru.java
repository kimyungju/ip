package haru;

import java.io.IOException;

/**
 * Main class for the Haru task management application.
 * Coordinates between UI, storage, and task list components.
 */
public class Haru {
    private Storage storage;
    private TaskList tasks;
    private ContactList contacts;
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
            contacts = new ContactList(storage.loadContacts());
        } catch (IOException e) {
            ui.showLoadingError();
            tasks = new TaskList();
            contacts = new ContactList();
        }
    }

    /**
     * Runs the main application loop.
     * Displays welcome message, reads and processes user commands until exit.
     */
    public void run() {
        ui.showWelcome();
        while (true) {
            String input = ui.readCommand();
            if (Parser.parseCommand(input) == Parser.Command.BYE) {
                ui.showGoodbye();
                ui.close();
                return;
            }
            System.out.println(getResponse(input));
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
        try {
            Parser.Command cmd = Parser.parseCommand(input);
            return executeCommand(cmd, input);
        } catch (HaruException e) {
            return "[ERROR] " + e.getMessage();
        } catch (NumberFormatException e) {
            return "[ERROR] Please use a valid number.";
        } catch (IOException e) {
            return "[ERROR] I couldn't save tasks to disk.";
        }
    }

    private String executeCommand(Parser.Command cmd, String input) throws HaruException, IOException {
        switch (cmd) {
        case BYE:
            return "Bye. Hope to see you again soon!";
        case LIST:
            return handleList();
        case MARK:
            return handleMark(input);
        case UNMARK:
            return handleUnmark(input);
        case DELETE:
            return handleDelete(input);
        case TODO:
            return handleTodo(input);
        case DEADLINE:
            return handleDeadline(input);
        case EVENT:
            return handleEvent(input);
        case FIND:
            return handleFind(input);
        case CONTACT:
            return handleContact(input);
        case CONTACT_LIST:
            return handleContactList();
        case CONTACT_DELETE:
            return handleContactDelete(input);
        case CONTACT_FIND:
            return handleContactFind(input);
        default:
            throw new HaruException("I'm sorry, but I don't know what that means :-(");
        }
    }

    private String handleList() {
        if (tasks.size() == 0) {
            return "Your task list is empty.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString();
    }

    private String handleMark(String input) throws HaruException, IOException {
        int idx = Parser.parseTaskIndex(input, Parser.MARK_PREFIX_LENGTH, tasks.size());
        tasks.get(idx).markAsDone();
        storage.saveAll(tasks, contacts);
        return "Nice! I've marked this task as done:\n  " + tasks.get(idx);
    }

    private String handleUnmark(String input) throws HaruException, IOException {
        int idx = Parser.parseTaskIndex(input, Parser.UNMARK_PREFIX_LENGTH, tasks.size());
        tasks.get(idx).markAsNotDone();
        storage.saveAll(tasks, contacts);
        return "OK, I've marked this task as not done yet:\n  " + tasks.get(idx);
    }

    private String handleDelete(String input) throws HaruException, IOException {
        int idx = Parser.parseTaskIndex(input, Parser.DELETE_PREFIX_LENGTH, tasks.size());
        Task removedTask = tasks.remove(idx);
        storage.saveAll(tasks, contacts);
        return "Noted. I've removed this task:\n  " + removedTask + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    private String handleTodo(String input) throws HaruException, IOException {
        Task todo = Parser.parseTodo(input);
        tasks.add(todo);
        storage.saveAll(tasks, contacts);
        return formatTaskAdded(todo);
    }

    private String handleDeadline(String input) throws HaruException, IOException {
        Task deadline = Parser.parseDeadline(input);
        tasks.add(deadline);
        storage.saveAll(tasks, contacts);
        return formatTaskAdded(deadline);
    }

    private String handleEvent(String input) throws HaruException, IOException {
        Task event = Parser.parseEvent(input);
        tasks.add(event);
        storage.saveAll(tasks, contacts);
        return formatTaskAdded(event);
    }

    private String formatTaskAdded(Task task) {
        return "Got it. I've added this task:\n  " + task + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    private String handleFind(String input) throws HaruException {
        if (input.length() <= Parser.FIND_PREFIX_LENGTH) {
            throw new HaruException("Please provide a keyword to search for.");
        }
        String keyword = input.substring(Parser.FIND_PREFIX_LENGTH).trim();
        if (keyword.isEmpty()) {
            throw new HaruException("Please provide a keyword to search for.");
        }
        TaskList matchingTasks = tasks.findTasks(keyword);
        if (matchingTasks.size() == 0) {
            return "No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            sb.append(i + 1).append(". ").append(matchingTasks.get(i)).append("\n");
        }
        return sb.toString();
    }

    private String handleContact(String input) throws HaruException, IOException {
        Contact newContact = Parser.parseContact(input);
        contacts.add(newContact);
        storage.saveAll(tasks, contacts);
        return "Got it. I've added this contact:\n  " + newContact + "\n"
                + "Now you have " + contacts.size() + " contacts in the list.";
    }

    private String handleContactList() {
        if (contacts.size() == 0) {
            return "Your contact list is empty.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the contacts in your list:\n");
        for (int i = 0; i < contacts.size(); i++) {
            sb.append(i + 1).append(". ").append(contacts.get(i)).append("\n");
        }
        return sb.toString();
    }

    private String handleContactDelete(String input) throws HaruException, IOException {
        int idx = Parser.parseTaskIndex(input, Parser.CONTACT_DELETE_PREFIX_LENGTH, contacts.size());
        Contact removedContact = contacts.remove(idx);
        storage.saveAll(tasks, contacts);
        return "Noted. I've removed this contact:\n  " + removedContact + "\n"
                + "Now you have " + contacts.size() + " contacts in the list.";
    }

    private String handleContactFind(String input) throws HaruException {
        if (input.length() <= Parser.CONTACT_FIND_PREFIX_LENGTH) {
            throw new HaruException("Please provide a keyword to search for.");
        }
        String contactKeyword = input.substring(Parser.CONTACT_FIND_PREFIX_LENGTH).trim();
        if (contactKeyword.isEmpty()) {
            throw new HaruException("Please provide a keyword to search for.");
        }
        ContactList matchingContacts = contacts.findContacts(contactKeyword);
        if (matchingContacts.size() == 0) {
            return "No matching contacts found.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the matching contacts in your list:\n");
        for (int i = 0; i < matchingContacts.size(); i++) {
            sb.append(i + 1).append(". ").append(matchingContacts.get(i)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns the welcome message for the GUI.
     *
     * @return The welcome message string.
     */
    public String getWelcomeMessage() {
        return "Hello! I'm Haru, your task manager.\nWhat can I do for you today?";
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
