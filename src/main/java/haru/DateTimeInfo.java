package haru;

import java.time.LocalDateTime;

public class DateTimeInfo {
    private final LocalDateTime dateTime;
    private final boolean hasTime;

    public DateTimeInfo(LocalDateTime dateTime, boolean hasTime) {
        this.dateTime = dateTime;
        this.hasTime = hasTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean hasTime() {
        return hasTime;
    }
}
