package haru;

/**
 * Handles parsing of user input commands and creation of tasks.
 */
public class Parser {
    /**
     * Enum representing all possible commands.
     */
    public enum Command {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, FIND,
        CONTACT, CONTACT_LIST, CONTACT_DELETE, CONTACT_FIND, UNKNOWN
    }

    static final int TODO_PREFIX_LENGTH = 5;
    static final int MARK_PREFIX_LENGTH = 5;
    static final int EVENT_PREFIX_LENGTH = 6;
    static final int UNMARK_PREFIX_LENGTH = 7;
    static final int DELETE_PREFIX_LENGTH = 7;
    static final int DEADLINE_PREFIX_LENGTH = 9;
    static final int FIND_PREFIX_LENGTH = 5;
    static final int CONTACT_PREFIX_LENGTH = 8;
    static final int CONTACT_DELETE_PREFIX_LENGTH = 15;
    static final int CONTACT_FIND_PREFIX_LENGTH = 13;
    private static final String PHONE_DELIMITER = " /phone ";
    private static final String EMAIL_DELIMITER = " /email ";

    /**
     * Parses the command word from user input.
     *
     * @param input The user input string.
     * @return The corresponding Command enum value, or UNKNOWN if not recognized.
     */
    public static Command parseCommand(String input) {
        assert input != null : "Parser input should not be null";
        String commandWord = input.split(" ")[0].toUpperCase();
        try {
            return Command.valueOf(commandWord);
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }

    /**
     * Parses and validates a task index from user input.
     *
     * @param input The user input string.
     * @param prefixLength The length of the command prefix.
     * @param taskCount The total number of tasks (for bounds checking).
     * @return The zero-based index of the task.
     * @throws HaruException If the index is missing, invalid, or out of bounds.
     */
    public static int parseTaskIndex(String input, int prefixLength, int taskCount) throws HaruException {
        assert prefixLength >= 0 : "Command prefix length should not be negative";
        if (input.length() <= prefixLength) {
            throw new HaruException("Please provide a task number.");
        }
        int index = Integer.parseInt(input.substring(prefixLength).trim()) - 1;
        if (index < 0 || index >= taskCount) {
            throw new HaruException("That task number doesn't exist.");
        }
        return index;
    }

    /**
     * Parses a TODO command and creates a Todo task.
     *
     * @param input The user input string.
     * @return A new Todo task.
     * @throws HaruException If the description is empty.
     */
    public static Task parseTodo(String input) throws HaruException {
        if (input.length() <= TODO_PREFIX_LENGTH) {
            throw new HaruException("The description of a todo cannot be empty.");
        }
        String description = input.substring(TODO_PREFIX_LENGTH).trim();
        if (description.isEmpty()) {
            throw new HaruException("The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    /**
     * Parses a DEADLINE command and creates a Deadline task.
     *
     * @param input The user input string.
     * @return A new Deadline task.
     * @throws HaruException If the format is incorrect or date/time is invalid.
     */
    public static Task parseDeadline(String input) throws HaruException {
        if (!input.contains(" /by ")) {
            throw new HaruException("A deadline needs a time! Use: /by");
        }
        String[] parts = input.substring(DEADLINE_PREFIX_LENGTH).split(" /by ", 2);
        if (parts.length < 2) {
            throw new HaruException("A deadline needs a time! Use: /by");
        }
        String description = parts[0].trim();
        if (description.isEmpty()) {
            throw new HaruException("The description of a deadline cannot be empty.");
        }
        DateTimeInfo byInfo = DateTimeUtil.parseUserInput(parts[1]);
        return new Deadline(description, byInfo);
    }

    /**
     * Parses an EVENT command and creates an Event task.
     *
     * @param input The user input string.
     * @return A new Event task.
     * @throws HaruException If the format is incorrect or date/time is invalid.
     */
    public static Task parseEvent(String input) throws HaruException {
        if (!input.contains(" /from ") || !input.contains(" /to ")) {
            throw new HaruException("Events need /from and /to.");
        }
        String[] parts = input.substring(EVENT_PREFIX_LENGTH).split(" /from | /to ");
        if (parts.length < 3) {
            throw new HaruException("Events need /from and /to.");
        }
        String description = parts[0].trim();
        if (description.isEmpty()) {
            throw new HaruException("The description of an event cannot be empty.");
        }
        DateTimeInfo fromInfo = DateTimeUtil.parseUserInput(parts[1].trim());
        DateTimeInfo toInfo = DateTimeUtil.parseUserInput(parts[2].trim());
        return new Event(description, fromInfo, toInfo);
    }

    /**
     * Parses a CONTACT command and creates a Contact.
     *
     * @param input The user input string.
     * @return A new Contact.
     * @throws HaruException If the format is incorrect.
     */
    public static Contact parseContact(String input) throws HaruException {
        if (!input.contains(PHONE_DELIMITER) || !input.contains(EMAIL_DELIMITER)) {
            throw new HaruException("A contact needs /phone and /email. "
                    + "Use: contact NAME /phone PHONE /email EMAIL");
        }
        String body = input.substring(CONTACT_PREFIX_LENGTH);
        int phoneIdx = body.indexOf(PHONE_DELIMITER);
        int emailIdx = body.indexOf(EMAIL_DELIMITER);
        String name = body.substring(0, Math.min(phoneIdx, emailIdx)).trim();
        if (name.isEmpty()) {
            throw new HaruException("The name of a contact cannot be empty.");
        }
        String phone;
        String email;
        if (phoneIdx < emailIdx) {
            phone = body.substring(phoneIdx + PHONE_DELIMITER.length(), emailIdx).trim();
            email = body.substring(emailIdx + EMAIL_DELIMITER.length()).trim();
        } else {
            email = body.substring(emailIdx + EMAIL_DELIMITER.length(), phoneIdx).trim();
            phone = body.substring(phoneIdx + PHONE_DELIMITER.length()).trim();
        }
        if (phone.isEmpty() || email.isEmpty()) {
            throw new HaruException("Phone and email cannot be empty.");
        }
        return new Contact(name, phone, email);
    }
}
