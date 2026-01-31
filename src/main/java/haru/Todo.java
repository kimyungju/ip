package haru;

/**
 * Represents a todo task (a task without any date/time attached).
 */
public class Todo extends Task {
    /**
     * Constructs a new Todo task.
     *
     * @param description The description of the todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of this todo.
     * Format: [T][X] description or [T][ ] description.
     *
     * @return The string representation with [T] prefix.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
