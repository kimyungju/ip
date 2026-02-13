package haru;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }
        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) {
                continue;
            }
            Task task = parseLine(trimmedLine);
            if (task != null) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    /**
     * Loads contacts from the storage file (lines starting with "C").
     *
     * @return An ArrayList of contacts parsed from the file.
     * @throws IOException If reading the file fails.
     */
    public ArrayList<Contact> loadContacts() throws IOException {
        ArrayList<Contact> contacts = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return contacts;
        }
        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            String trimmedLine = line.trim();
            if (trimmedLine.isEmpty()) {
                continue;
            }
            Contact contact = parseContactLine(trimmedLine);
            if (contact != null) {
                contacts.add(contact);
            }
        }
        return contacts;
    }

    public void save(TaskList tasks) throws IOException {
        assert tasks != null : "TaskList to save should not be null";
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            lines.add(formatLine(tasks.get(i)));
        }
        Files.write(filePath, lines);
    }

    /**
     * Saves both tasks and contacts to the storage file.
     *
     * @param tasks The task list to save.
     * @param contacts The contact list to save.
     * @throws IOException If writing to the file fails.
     */
    public void saveAll(TaskList tasks, ContactList contacts) throws IOException {
        assert tasks != null : "TaskList to save should not be null";
        assert contacts != null : "ContactList to save should not be null";
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            lines.add(formatLine(tasks.get(i)));
        }
        for (int i = 0; i < contacts.size(); i++) {
            lines.add(formatContactLine(contacts.get(i)));
        }
        Files.write(filePath, lines);
    }

    private Task parseLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }
        String type = parts[0];
        String status = parts[1];
        String description = parts[2];
        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                return null;
            }
            DateTimeInfo byInfo = parseDateTimeSafely(parts[3]);
            if (byInfo == null) {
                return null;
            }
            task = new Deadline(description, byInfo);
            break;
        case "E":
            if (parts.length < 5) {
                return null;
            }
            DateTimeInfo fromInfo = parseDateTimeSafely(parts[3]);
            DateTimeInfo toInfo = parseDateTimeSafely(parts[4]);
            if (fromInfo == null || toInfo == null) {
                return null;
            }
            task = new Event(description, fromInfo, toInfo);
            break;
        default:
            return null;
        }

        if ("1".equals(status)) {
            task.markAsDone();
        } else if (!"0".equals(status)) {
            return null;
        }
        return task;
    }

    /**
     * Joins storage line parts with " | " separator.
     *
     * @param parts The parts to join.
     * @return The joined string.
     */
    private static String joinStorageParts(String... parts) {
        return String.join(" | ", parts);
    }

    private String formatLine(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Event) {
            Event event = (Event) task;
            return joinStorageParts("E", status, event.getDescription(),
                    event.getFromStorageString(), event.getToStorageString());
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return joinStorageParts("D", status, deadline.getDescription(),
                    deadline.getByStorageString());
        }
        return joinStorageParts("T", status, task.getDescription());
    }

    private Contact parseContactLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 4 || !"C".equals(parts[0])) {
            return null;
        }
        return new Contact(parts[1], parts[2], parts[3]);
    }

    private String formatContactLine(Contact contact) {
        return joinStorageParts("C", contact.getName(), contact.getPhone(), contact.getEmail());
    }

    private DateTimeInfo parseDateTimeSafely(String value) {
        try {
            return DateTimeUtil.parseStorage(value);
        } catch (Exception e) {
            return null;
        }
    }
}
