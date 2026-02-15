package haru;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Formats plain-text responses into styled JavaFX nodes for the chat GUI.
 */
public class ResponseFormatter {
    private static final Pattern NUMBERED_TASK = Pattern.compile(
            "(\\d+)\\. \\[([TDE])\\]\\[([X ])\\] (.+)");
    private static final Pattern TASK = Pattern.compile(
            "\\[([TDE])\\]\\[([X ])\\] (.+)");
    private static final Pattern NUMBERED_CONTACT = Pattern.compile(
            "(\\d+)\\. (.+) \\(phone: (.+), email: (.+)\\)");
    private static final Pattern CONTACT = Pattern.compile(
            "(.+) \\(phone: (.+), email: (.+)\\)");
    private static final Pattern COUNT = Pattern.compile(
            "Now you have \\d+ (tasks|contacts) in the list\\.");

    /**
     * Parses a response string and returns a styled JavaFX node tree.
     */
    public static Node format(String text) {
        VBox container = new VBox(6);
        for (String line : text.split("\n")) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                container.getChildren().add(parseLine(trimmed));
            }
        }
        return container;
    }

    private static Node parseLine(String line) {
        Matcher m;

        m = NUMBERED_TASK.matcher(line);
        if (m.matches()) {
            return buildTaskCard(m.group(1), m.group(2), "X".equals(m.group(3)), m.group(4));
        }

        m = TASK.matcher(line);
        if (m.matches()) {
            return buildTaskCard(null, m.group(1), "X".equals(m.group(2)), m.group(3));
        }

        m = NUMBERED_CONTACT.matcher(line);
        if (m.matches()) {
            return buildContactCard(m.group(1), m.group(2), m.group(3), m.group(4));
        }

        m = CONTACT.matcher(line);
        if (m.matches()) {
            return buildContactCard(null, m.group(1), m.group(2), m.group(3));
        }

        if (COUNT.matcher(line).matches()) {
            Label label = new Label(line);
            label.getStyleClass().add("response-count");
            label.setWrapText(true);
            return label;
        }

        Label label = new Label(line);
        label.getStyleClass().add(line.endsWith(":") ? "response-header" : "response-message");
        label.setWrapText(true);
        return label;
    }

    private static Node buildTaskCard(String number, String type, boolean done, String content) {
        String description = content;
        String dateInfo = null;
        int paren = findDateParen(content);
        if (paren >= 0) {
            description = content.substring(0, paren).trim();
            dateInfo = content.substring(paren + 1, content.length() - 1);
        }

        VBox card = new VBox(3);
        card.getStyleClass().addAll("task-card", "task-card-" + typeKey(type));

        HBox row = new HBox(6);
        row.setAlignment(Pos.CENTER_LEFT);

        if (number != null) {
            Label num = new Label(number);
            num.getStyleClass().add("task-number");
            row.getChildren().add(num);
        }

        Label badge = new Label(badgeText(type));
        badge.getStyleClass().addAll("task-badge", "badge-" + typeKey(type));
        row.getChildren().add(badge);

        Label desc = new Label(description);
        desc.getStyleClass().add("task-description");
        if (done) {
            desc.getStyleClass().add("task-done");
        }
        desc.setWrapText(true);
        HBox.setHgrow(desc, Priority.ALWAYS);
        row.getChildren().add(desc);

        if (done) {
            Label check = new Label("\u2713");
            check.getStyleClass().add("task-check");
            row.getChildren().add(check);
        }

        card.getChildren().add(row);

        if (dateInfo != null) {
            Label date = new Label(formatDate(dateInfo));
            date.getStyleClass().add("task-date");
            date.setWrapText(true);
            card.getChildren().add(date);
        }

        return card;
    }

    private static Node buildContactCard(String number, String name, String phone, String email) {
        VBox card = new VBox(3);
        card.getStyleClass().addAll("task-card", "task-card-contact");

        HBox nameRow = new HBox(6);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        if (number != null) {
            Label num = new Label(number);
            num.getStyleClass().add("task-number");
            nameRow.getChildren().add(num);
        }

        Label badge = new Label("CONTACT");
        badge.getStyleClass().addAll("task-badge", "badge-contact");
        nameRow.getChildren().add(badge);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("contact-name");
        nameLabel.setWrapText(true);
        nameRow.getChildren().add(nameLabel);

        card.getChildren().add(nameRow);

        HBox detailRow = new HBox(6);
        detailRow.setAlignment(Pos.CENTER_LEFT);
        Label ph = new Label(phone);
        ph.getStyleClass().add("contact-detail");
        Label sep = new Label("\u00B7");
        sep.getStyleClass().add("contact-detail-sep");
        Label em = new Label(email);
        em.getStyleClass().add("contact-detail");
        detailRow.getChildren().addAll(ph, sep, em);
        card.getChildren().add(detailRow);

        return card;
    }

    private static String badgeText(String type) {
        switch (type) {
        case "T": return "TODO";
        case "D": return "DUE";
        case "E": return "EVENT";
        default: return type;
        }
    }

    private static String typeKey(String type) {
        switch (type) {
        case "T": return "todo";
        case "D": return "deadline";
        case "E": return "event";
        default: return "todo";
        }
    }

    private static String formatDate(String info) {
        if (info.startsWith("by: ")) {
            return info.substring(4);
        }
        if (info.startsWith("from: ")) {
            return info.substring(6).replace(" to: ", "  \u2192  ");
        }
        return info;
    }

    private static int findDateParen(String content) {
        if (!content.endsWith(")")) {
            return -1;
        }
        int depth = 0;
        for (int i = content.length() - 1; i >= 0; i--) {
            if (content.charAt(i) == ')') {
                depth++;
            } else if (content.charAt(i) == '(') {
                depth--;
                if (depth == 0) {
                    String inner = content.substring(i + 1);
                    if (inner.startsWith("by: ") || inner.startsWith("from: ")) {
                        return i;
                    }
                    return -1;
                }
            }
        }
        return -1;
    }
}
