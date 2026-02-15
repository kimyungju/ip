package haru;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Represents a dialog box consisting of an avatar and a styled message bubble.
 */
public class DialogBox extends HBox {
    private static final double AVATAR_SIZE = 40.0;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("h:mm a");

    private DialogBox(String s, Image img, String styleClass) {
        Label text = new Label(s);
        text.setWrapText(true);
        text.setMinHeight(Label.USE_PREF_SIZE);
        text.getStyleClass().add("dialog-text");

        Label timestamp = new Label(LocalTime.now().format(TIME_FORMAT));
        timestamp.getStyleClass().add("dialog-timestamp");

        VBox bubble = new VBox(text, timestamp);
        bubble.getStyleClass().addAll("dialog-bubble", styleClass + "-bubble");

        ImageView displayPicture = new ImageView(img);
        displayPicture.setFitWidth(AVATAR_SIZE);
        displayPicture.setFitHeight(AVATAR_SIZE);

        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(
            AVATAR_SIZE / 2, AVATAR_SIZE / 2, AVATAR_SIZE / 2
        );
        displayPicture.setClip(clip);

        this.setAlignment(Pos.TOP_RIGHT);
        this.setSpacing(10);
        this.getStyleClass().add("dialog-container");
        this.getChildren().addAll(bubble, displayPicture);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    public static DialogBox getUserDialog(String s, Image img) {
        return new DialogBox(s, img, "user");
    }

    public static DialogBox getHaruDialog(String s, Image img) {
        DialogBox db = new DialogBox(s, img, "haru");
        db.flip();
        return db;
    }

    public static DialogBox getHaruErrorDialog(String s, Image img) {
        DialogBox db = new DialogBox(s, img, "error");
        db.flip();
        return db;
    }
}
