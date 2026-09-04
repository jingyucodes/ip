package echo.gui;

import echo.Echo;
import echo.parser.Parser;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for {@code MainWindow.fxml}: turns each Enter/Send into one
 * round trip through {@link Echo#getResponse(String)}, and renders the
 * result as a pair of chat bubbles.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Echo echo;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.jpeg"));
    private final Image echoImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    @FXML
    private void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Echo instance this window talks to, and shows Echo's
     * opening greeting as the first bubble.
     */
    public void setEcho(Echo echo) {
        this.echo = echo;
        dialogContainer.getChildren().add(
                DialogBox.getEchoDialog("Hello! I'm Echo. What's on your mind?", echoImage, ""));
    }

    /**
     * Sends the text field's contents to Echo, shows the exchange as a
     * pair of dialog boxes, then clears the field. A blank input is
     * ignored rather than sent, matching what pressing Enter on an
     * empty console line does. Typing {@code bye} closes the window
     * shortly after showing Echo's farewell, the same as the console UI.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        String commandWord = Parser.getCommandWord(input.trim());
        String response = echo.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getEchoDialog(response, echoImage, commandWord));
        userInput.clear();

        if (input.trim().equals("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1.2));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
