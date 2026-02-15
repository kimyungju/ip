package haru;

/**
 * Represents a task with a description and completion status.
 * This is the base class for all specific task types.
 */
public class Task {
    private String description;
    protected boolean isDone;

    /**
     * Constructs a new task with the given description.
     * The task is initially not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Task description must not be null");
        }
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns whether this task is done.
     *
     * @return true if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns a string representation of this task.
     * Format: [X] description (if done) or [ ] description (if not done).
     *
     * @return The string representation of this task.
     */
    @Override
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }
}
