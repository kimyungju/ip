package haru;

public class Parser {
    public enum Command {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN
    }

    public static Command parseCommand(String input) {
        String commandWord = input.split(" ")[0].toUpperCase();
        try {
            return Command.valueOf(commandWord);
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }

    public static int parseTaskIndex(String input, int offset, int taskCount) throws HaruException {
        if (input.length() <= offset) {
            throw new HaruException("Please provide a task number.");
        }
        int index = Integer.parseInt(input.substring(offset).trim()) - 1;
        if (index < 0 || index >= taskCount) {
            throw new HaruException("That task number doesn't exist.");
        }
        return index;
    }

    public static Task parseTodo(String input) throws HaruException {
        if (input.length() <= 5) {
            throw new HaruException("The description of a todo cannot be empty.");
        }
        return new Todo(input.substring(5).trim());
    }

    public static Task parseDeadline(String input) throws HaruException {
        if (!input.contains(" /by ")) {
            throw new HaruException("A deadline needs a time! Use: /by");
        }
        String[] parts = input.substring(9).split(" /by ", 2);
        if (parts.length < 2) {
            throw new HaruException("A deadline needs a time! Use: /by");
        }
        DateTimeInfo byInfo = DateTimeUtil.parseUserInput(parts[1]);
        return new Deadline(parts[0].trim(), byInfo);
    }

    public static Task parseEvent(String input) throws HaruException {
        if (!input.contains(" /from ") || !input.contains(" /to ")) {
            throw new HaruException("Events need /from and /to.");
        }
        String[] parts = input.substring(6).split(" /from | /to ");
        if (parts.length < 3) {
            throw new HaruException("Events need /from and /to.");
        }
        DateTimeInfo fromInfo = DateTimeUtil.parseUserInput(parts[1].trim());
        DateTimeInfo toInfo = DateTimeUtil.parseUserInput(parts[2].trim());
        return new Event(parts[0].trim(), fromInfo, toInfo);
    }
}
