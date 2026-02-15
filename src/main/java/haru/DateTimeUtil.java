package haru;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter INPUT_DATETIME_COLON = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter OUTPUT_DATETIME = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    public static DateTimeInfo parseUserInput(String input) throws HaruException {
        if (input == null) {
            throw new IllegalArgumentException("Date/time input must not be null");
        }
        String trimmed = input.trim();
        try {
            LocalDateTime dateTime = LocalDateTime.parse(trimmed, INPUT_DATETIME);
            return new DateTimeInfo(dateTime, true);
        } catch (DateTimeParseException e) {
            // fall through
        }
        try {
            LocalDateTime dateTime = LocalDateTime.parse(trimmed, INPUT_DATETIME_COLON);
            return new DateTimeInfo(dateTime, true);
        } catch (DateTimeParseException e) {
            // fall through
        }
        try {
            LocalDate date = LocalDate.parse(trimmed, INPUT_DATE);
            return new DateTimeInfo(date.atStartOfDay(), false);
        } catch (DateTimeParseException e) {
            throw new HaruException("Please use date format yyyy-MM-dd or yyyy-MM-dd HHmm.");
        }
    }

    public static DateTimeInfo parseStorage(String input) throws DateTimeParseException {
        if (input == null) {
            throw new IllegalArgumentException("Storage date/time string must not be null");
        }
        try {
            LocalDateTime dateTime = LocalDateTime.parse(input);
            return new DateTimeInfo(dateTime, true);
        } catch (DateTimeParseException e) {
            LocalDate date = LocalDate.parse(input);
            return new DateTimeInfo(date.atStartOfDay(), false);
        }
    }

    public static String formatForDisplay(LocalDateTime dateTime, boolean hasTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime must not be null");
        }
        if (hasTime) {
            return dateTime.format(OUTPUT_DATETIME);
        }
        return dateTime.format(OUTPUT_DATE);
    }

    public static String formatForStorage(LocalDateTime dateTime, boolean hasTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime must not be null");
        }
        if (hasTime) {
            return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return dateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
