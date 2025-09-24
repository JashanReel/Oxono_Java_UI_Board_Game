package g62727.dev3.oxono.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Represents the menu of the game
 * Responsible for starting a new game, surrendering, undoing/redoing an action
 */
public class MenuView extends HBox {
    /**
     * Attributes of the MenuView class
     */
    private Button newGameButton;
    private Button surrenderButton;
    private Button undoButton;
    private Button redoButton;
    private Button refreshButton;
    private Button musicButton;

    /**
     * Constructs a new MenuView with styled buttons for game actions.
     */
    MenuView() {
        newGameButton = createStyledButton("Nouvelle partie");
        surrenderButton = createStyledButton("Abandonner");
        undoButton = createStyledButton("Undo");
        redoButton = createStyledButton("Redo");
        refreshButton = createStyledButton("Revanche");
        musicButton = createStyledButton("Musique On/Off");
        getChildren().addAll(newGameButton, surrenderButton, undoButton, redoButton, refreshButton, musicButton);
    }

    /**
     * Sets the action for the new game button.
     * @param handler The event handler for the new game action
     */
    void setNewGameAction(EventHandler<ActionEvent> handler) {
        newGameButton.setOnAction(handler);
    }

    /**
     * Sets the action for the surrender button.
     * @param handler The event handler for the surrender action
     */
    void setSurrenderAction(EventHandler<ActionEvent> handler) {
        surrenderButton.setOnAction(handler);
    }

    /**
     * Sets the action for the undo button.
     * @param handler The event handler for the undo action
     */
    void setUndoAction(EventHandler<ActionEvent> handler) {
        undoButton.setOnAction(handler);
    }

    /**
     * Sets the action for the redo button.
     * @param handler The event handler for the redo action
     */
    void setRedoAction(EventHandler<ActionEvent> handler) {
        redoButton.setOnAction(handler);
    }

    /**
     * Sets the action for the refresh button.
     * @param handler The event handler for the refresh action
     */
    void setRefreshAction(EventHandler<ActionEvent> handler) {
        refreshButton.setOnAction(handler);
    }

    /**
     * Sets the action for the music toggle button.
     * @param handler The event handler for the music toggle action
     */
    void setMusicAction(EventHandler<ActionEvent> handler) {
        musicButton.setOnAction(handler);
    }

    /**
     * Creates and returns an HBox containing the top row of buttons.
     * @return An HBox with new game, surrender, and music buttons
     */
    HBox createTopButtons() {
        HBox topButtons = new HBox(10);
        topButtons.getChildren().addAll(newGameButton, surrenderButton, musicButton);
        topButtons.setAlignment(Pos.CENTER);
        return topButtons;
    }

    /**
     * Creates and returns an HBox containing the bottom row of buttons.
     * @return An HBox with undo, redo, and refresh buttons
     */
    HBox createBottomButtons() {
        HBox bottomButtons = new HBox(10);
        bottomButtons.getChildren().addAll(undoButton, redoButton, refreshButton);
        bottomButtons.setAlignment(Pos.CENTER);
        return bottomButtons;
    }

    /**
     * Disables the surrender button.
     */
    void hideSurrenderButton() {
        surrenderButton.setDisable(true);
    }

    /**
     * Enables the surrender button.
     */
    void showSurrenderButton() {
        surrenderButton.setDisable(false);
    }

    /**
     * Enables the refresh button.
     */
    void showRefreshButton() {
        refreshButton.setDisable(false);
    }

    /**
     * Disables the refresh button.
     */
    void hideRefreshButton() {
        refreshButton.setDisable(true);
    }

    /**
     * Enables the undo and redo buttons.
     */
    void showUndoRedoButtons() {
        undoButton.setDisable(false);
        redoButton.setDisable(false);
    }

    /**
     * Disables the undo and redo buttons.
     */
    void hideUndoRedoButtons() {
        undoButton.setDisable(true);
        redoButton.setDisable(true);
    }

    /**
     * Creates a styled button with the given text.
     * @param text The text to display on the button
     * @return A styled Button instance
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #333; " +
                "-fx-text-fill: white; " +
                "-fx-border-width: 2px; " +
                "-fx-border-color: white; " +
                "-fx-border-radius: 5; " +
                "-fx-background-radius: 5; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 10 20 10 20;");
        return button;
    }
}
