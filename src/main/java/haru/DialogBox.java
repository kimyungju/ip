package haru;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView and a label.
 */
public class DialogBox extends HBox {
    private Label text;
    private ImageView displayPicture;

    private static final double AVATAR_SIZE = 50.0;

    private DialogBox(String s, Image img) {
        text = new Label(s);
        text.setWrapText(true);
        text.setMinHeight(Label.USE_PREF_SIZE);

        displayPicture = new ImageView(img);
        displayPicture.setFitWidth(AVATAR_SIZE);
        displayPicture.setFitHeight(AVATAR_SIZE);

        // Make avatar circular
        javafx.scene.shape.Circle clip = new javafx.scene.shape.Circle(
            AVATAR_SIZE / 2, AVATAR_SIZE / 2, AVATAR_SIZE / 2
        );
        displayPicture.setClip(clip);

        this.setAlignment(Pos.TOP_RIGHT);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.getStyleClass().add("dialog-box");
        this.getChildren().addAll(text, displayPicture);
    }

    /**
     * Styles this dialog box as a user message (right-aligned, light blue background).
     */
    public void setUserStyle() {
        this.getStyleClass().add("user-message");
    }

    /**
     * Styles this dialog box as a Haru message (left-aligned, light gray background).
     */
    public void setHaruStyle() {
        this.getStyleClass().add("bot-message");
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
        DialogBox db = new DialogBox(s, img);
        db.setUserStyle();
        return db;
    }

    public static DialogBox getHaruDialog(String s, Image img) {
        DialogBox db = new DialogBox(s, img);
        db.flip();
        db.setHaruStyle();
        return db;
    }
}
