package g62727.dev3.oxono.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MessageView extends VBox {
    private List<Label> historyOfMsgs;

    public MessageView() {
        this.historyOfMsgs = new ArrayList<>();
        prepareMessageBox();
    }

    /**
     * Private method initializing the history box with "-" as placeholders at the beginning of every new match
     */
    private void prepareMessageBox() {
        setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: #333;");
        setPadding(new Insets(10));
        setSpacing(5);
        setAlignment(Pos.BOTTOM_CENTER);
        Label titleLabel = new Label("Historique");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20px;");
        getChildren().addAll(titleLabel);
        for (int i = 0; i < 5; i++) {
            Label newMessage = new Label("-");
            newMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
            historyOfMsgs.add(newMessage);
            getChildren().add(newMessage);
        }
    }

    /**
     * Resets the message history.
     */
    void resetHistory() {
        historyOfMsgs.clear();
        getChildren().clear();
        Label titleLabel = new Label("Historique");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20px;");
        getChildren().addAll(titleLabel);
        for (int i = 0; i < 5; i++) {
            Label placeholderLabel = new Label("-");
            placeholderLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
            historyOfMsgs.add(placeholderLabel);
            getChildren().add(placeholderLabel);
        }
    }

    /**
     * Updates the message history with a new message.
     * @param message The new message to be added
     */
    void updateMessage(String message) {
        Label newMessage = new Label(message);
        newMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        historyOfMsgs.add(newMessage);
        if (historyOfMsgs.size() > 5) {
            Label oldestMessage = historyOfMsgs.removeFirst();
            getChildren().remove(oldestMessage);
        }
        getChildren().add(newMessage);
    }
}
