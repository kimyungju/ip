package haru;

import java.time.LocalDateTime;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;
    protected boolean hasTimeFrom;
    protected boolean hasTimeTo;

    public Event(String description, DateTimeInfo fromInfo, DateTimeInfo toInfo) {
        super(description);
        this.from = fromInfo.getDateTime();
        this.to = toInfo.getDateTime();
        this.hasTimeFrom = fromInfo.hasTime();
        this.hasTimeTo = toInfo.hasTime();
    }

    public String getFromStorageString() {
        return DateTimeUtil.formatForStorage(from, hasTimeFrom);
    }

    public String getToStorageString() {
        return DateTimeUtil.formatForStorage(to, hasTimeTo);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + DateTimeUtil.formatForDisplay(from, hasTimeFrom)
                + " to: " + DateTimeUtil.formatForDisplay(to, hasTimeTo) + ")";
    }
}
