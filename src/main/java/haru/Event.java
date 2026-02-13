package haru;

import java.time.LocalDateTime;

/**
 * Represents an event task (a task with start and end date/time).
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;
    protected boolean hasTimeFrom;
    protected boolean hasTimeTo;

    /**
     * Constructs a new Event task.
     *
     * @param description The description of the event.
     * @param fromInfo The start date/time information.
     * @param toInfo The end date/time information.
     */
    public Event(String description, DateTimeInfo fromInfo, DateTimeInfo toInfo) {
        super(description);
        assert fromInfo != null : "Event from DateTimeInfo should not be null";
        assert toInfo != null : "Event to DateTimeInfo should not be null";
        this.from = fromInfo.getDateTime();
        this.to = toInfo.getDateTime();
        this.hasTimeFrom = fromInfo.hasTime();
        this.hasTimeTo = toInfo.hasTime();
    }

    /**
     * Returns the start date/time in storage format.
     *
     * @return The formatted string for storage.
     */
    public String getFromStorageString() {
        return DateTimeUtil.formatForStorage(from, hasTimeFrom);
    }

    /**
     * Returns the end date/time in storage format.
     *
     * @return The formatted string for storage.
     */
    public String getToStorageString() {
        return DateTimeUtil.formatForStorage(to, hasTimeTo);
    }

    /**
     * Returns a string representation of this event.
     * Format: [E][X] description (from: date to: date) or [E][ ] description (from: date to: date).
     *
     * @return The string representation with [E] prefix and time range.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + DateTimeUtil.formatForDisplay(from, hasTimeFrom)
                + " to: " + DateTimeUtil.formatForDisplay(to, hasTimeTo) + ")";
    }
}
