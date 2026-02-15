package haru;

import java.io.InputStream;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A GUI for Haru using JavaFX.
 */
public class Main extends Application {
    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;

    private Image userImage;
    private Image haruImage;
    private Haru haru = new Haru("data/haru.txt");

    @Override
    public void start(Stage stage) {
        userImage = loadImage("/images/DaUser.png");
        haruImage = loadImage("/images/Haru.png");

        // Header bar
        ImageView headerAvatar = new ImageView(haruImage);
        headerAvatar.setFitWidth(36);
        headerAvatar.setFitHeight(36);
        javafx.scene.shape.Circle headerClip = new javafx.scene.shape.Circle(18, 18, 18);
        headerAvatar.setClip(headerClip);

        Label titleLabel = new Label("Haru");
        titleLabel.getStyleClass().add("header-title");

        Label subtitleLabel = new Label("Task Manager");
        subtitleLabel.getStyleClass().add("header-subtitle");

        VBox titleBox = new VBox(titleLabel, subtitleLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox headerBar = new HBox(12, headerAvatar, titleBox);
        headerBar.getStyleClass().add("header-bar");
        headerBar.setAlignment(Pos.CENTER_LEFT);
        headerBar.setPadding(new Insets(10, 16, 10, 16));

        // Chat area
        dialogContainer = new VBox();
        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        dialogContainer.setSpacing(12);
        dialogContainer.setPadding(new Insets(15));

        scrollPane = new ScrollPane(dialogContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        // Input bar
        userInput = new TextField();
        userInput.setPromptText("Type a message...");
        userInput.getStyleClass().add("input-field");
        HBox.setHgrow(userInput, Priority.ALWAYS);

        sendButton = new Button("\u27A4");
        sendButton.getStyleClass().add("send-button");

        HBox inputBar = new HBox(10, userInput, sendButton);
        inputBar.getStyleClass().add("input-bar");
        inputBar.setAlignment(Pos.CENTER);
        inputBar.setPadding(new Insets(10, 12, 10, 12));

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(headerBar);
        mainLayout.setCenter(scrollPane);
        mainLayout.setBottom(inputBar);

        Scene scene = new Scene(mainLayout, 420, 650);

        try {
            scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("Could not load stylesheet: " + e.getMessage());
        }

        stage.setScene(scene);
        stage.setTitle("Haru");
        stage.setResizable(true);
        stage.setMinWidth(380);
        stage.setMinHeight(500);

        try {
            stage.getIcons().add(haruImage);
        } catch (Exception e) {
            // Ignore if icon not available
        }

        stage.show();

        // Welcome message
        dialogContainer.getChildren().add(
                DialogBox.getHaruDialog(haru.getWelcomeMessage(), haruImage));

        // Auto-scroll on new content
        dialogContainer.heightProperty().addListener((observable) -> scrollPane.setVvalue(1.0));

        // Input handlers
        sendButton.setOnMouseClicked((event) -> handleUserInput());
        userInput.setOnAction((event) -> handleUserInput());

        Platform.runLater(() -> userInput.requestFocus());
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
        WritableImage placeholder = new WritableImage(40, 40);
        PixelWriter pw = placeholder.getPixelWriter();
        for (int x = 0; x < 40; x++) {
            for (int y = 0; y < 40; y++) {
                pw.setArgb(x, y, 0xFFE0E0E0);
            }
        }
        return placeholder;
    }

    private void handleUserInput() {
        String userText = userInput.getText().trim();
        if (userText.isEmpty()) {
            return;
        }

        String haruText = haru.getResponse(userText);

        dialogContainer.getChildren().add(
                DialogBox.getUserDialog(userText, userImage));

        if (haruText.startsWith("[ERROR] ")) {
            dialogContainer.getChildren().add(
                    DialogBox.getHaruErrorDialog(haruText.substring(8), haruImage));
        } else {
            dialogContainer.getChildren().add(
                    DialogBox.getHaruDialog(haruText, haruImage));
        }

        userInput.clear();

        if (userText.equalsIgnoreCase("bye")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition delay = new PauseTransition(Duration.millis(1500));
            delay.setOnFinished(e -> Platform.exit());
            delay.play();
        }
    }
}
