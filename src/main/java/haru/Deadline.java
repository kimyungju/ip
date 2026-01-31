package haru;

import java.time.LocalDateTime;

/**
 * Represents a deadline task (a task with a deadline date/time).
 */
public class Deadline extends Task {
    protected LocalDateTime by;
    protected boolean hasTime;

    /**
     * Constructs a new Deadline task.
     *
     * @param description The description of the deadline.
     * @param byInfo The date/time information for the deadline.
     */
    public Deadline(String description, DateTimeInfo byInfo) {
        super(description);
        this.by = byInfo.getDateTime();
        this.hasTime = byInfo.hasTime();
    }

    /**
     * Returns the deadline date/time in storage format.
     *
     * @return The formatted string for storage.
     */
    public String getByStorageString() {
        return DateTimeUtil.formatForStorage(by, hasTime);
    }

    /**
     * Returns a string representation of this deadline.
     * Format: [D][X] description (by: formatted date) or [D][ ] description (by: formatted date).
     *
     * @return The string representation with [D] prefix and deadline.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateTimeUtil.formatForDisplay(by, hasTime) + ")";
    }
}
