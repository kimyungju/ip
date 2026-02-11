package haru;

import java.io.InputStream;

import javafx.application.Application;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * A GUI for Haru using JavaFX.
 */
public class Main extends Application {
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    private Image userImage;
    private Image haruImage;
    private Haru haru = new Haru("data/haru.txt");

    @Override
    public void start(Stage stage) {
        // Load images (use placeholder if not found)
        userImage = loadImage("/images/DaUser.png");
        haruImage = loadImage("/images/Haru.png");

        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        stage.setTitle("Haru");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        // Scroll area: top and sides, leave bottom for input row
        scrollPane.setPrefSize(400.0, 535.0);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white;");

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.setSpacing(10);
        dialogContainer.setPadding(new Insets(10));

        // Input row: fixed height at bottom
        userInput.setPrefHeight(45.0);
        userInput.setPrefWidth(325.0);
        sendButton.setPrefHeight(45.0);
        sendButton.setPrefWidth(75.0);
        sendButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        // Anchors: scrollPane fills top, input row at bottom
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 55.0);

        AnchorPane.setBottomAnchor(sendButton, 5.0);
        AnchorPane.setRightAnchor(sendButton, 5.0);

        AnchorPane.setBottomAnchor(userInput, 5.0);
        AnchorPane.setLeftAnchor(userInput, 5.0);
        AnchorPane.setRightAnchor(userInput, 85.0);

        // Show welcome message
        dialogContainer.getChildren().add(
                DialogBox.getHaruDialog(haru.getWelcomeMessage(), haruImage));

        // Scroll down when new content added
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        // Handle user input
        sendButton.setOnMouseClicked((event) -> handleUserInput());
        userInput.setOnAction((event) -> handleUserInput());
    }

    private Image loadImage(String path) {
        try {
            InputStream stream = getClass().getResourceAsStream(path);
            if (stream != null) {
                return new Image(stream);
            }
        } catch (Exception e) {
            // Ignore
        }
        // Placeholder: 50x50 light gray square (matches avatar size)
        WritableImage placeholder = new WritableImage(50, 50);
        PixelWriter pw = placeholder.getPixelWriter();
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                pw.setArgb(x, y, 0xFFE0E0E0);
            }
        }
        return placeholder;
    }

    private void handleUserInput() {
        String userText = userInput.getText();
        String haruText = haru.getResponse(userText);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getHaruDialog(haruText, haruImage));

        userInput.clear();
    }
}
