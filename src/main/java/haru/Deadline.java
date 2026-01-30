package haru;

import java.time.LocalDateTime;

public class Deadline extends Task {
    protected LocalDateTime by;
    protected boolean hasTime;

    public Deadline(String description, DateTimeInfo byInfo) {
        super(description);
        this.by = byInfo.getDateTime();
        this.hasTime = byInfo.hasTime();
    }

    public String getByStorageString() {
        return DateTimeUtil.formatForStorage(by, hasTime);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateTimeUtil.formatForDisplay(by, hasTime) + ")";
    }
}
