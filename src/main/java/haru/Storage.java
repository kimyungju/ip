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

    public void save(ArrayList<Task> tasks) throws IOException {
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatLine(task));
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

    private String formatLine(Task task) {
        String status = task.isDone ? "1" : "0";
        if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + status + " | " + event.description
                    + " | " + event.getFromStorageString()
                    + " | " + event.getToStorageString();
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + status + " | " + deadline.description
                    + " | " + deadline.getByStorageString();
        }
        return "T | " + status + " | " + task.description;
    }

    private DateTimeInfo parseDateTimeSafely(String value) {
        try {
            return DateTimeUtil.parseStorage(value);
        } catch (Exception e) {
            return null;
        }
    }
}
