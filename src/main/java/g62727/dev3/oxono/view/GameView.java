package g62727.dev3.oxono.view;

import g62727.dev3.oxono.model.Game;
import g62727.dev3.oxono.model.GameState;
import g62727.dev3.oxono.model.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Represents the whole game window
 * The game info, the button menu and managing the board
 * Being an observer, it adapts the view automatically according to the changes of the observable
 */
public class GameView extends VBox {
    /**
     * Attributes of the GameView class
     */
    private Game game;
    private BoardView boardView;
    private InfoView infoView;
    private MenuView menuView;
    private MessageView messageView;

    /**
     * Constructs a new GameView with the specified board size.
     * @param boardSize The size of the game board
     */
    public GameView(int boardSize) {
        initializeAttributes(boardSize);
        HBox topButtons = createTopButtons();
        HBox bottomButtons = createBottomButtons();
        Region spacer = new Region();
        spacer.setPrefHeight(20);
        HBox mainLayout = setUpBoard();
        getChildren().addAll(topButtons, infoView, spacer, mainLayout, bottomButtons, messageView);
        setAlignment(Pos.CENTER);
    }

    /**
     * Method initializing the attributes and the different components of the view
     * @param boardSize - the size of the board to display
     */
    private void initializeAttributes(int boardSize) {
        boardView = new BoardView(boardSize);
        infoView = new InfoView();
        menuView = new MenuView();
        messageView = new MessageView();
    }

    /**
     * Private method setting up the board in a HBox
     * @return the layout composed of a HBox containing the boardView
     */
    private HBox setUpBoard() {
        HBox mainLayout = new HBox();
        mainLayout.getChildren().addAll(boardView);
        mainLayout.setAlignment(Pos.CENTER);
        return mainLayout;
    }

    /**
     * Private method creating the buttons at the top of the board : new game, surrender, toggle music
     * @return the buttons contained in a HBox
     */
    private HBox createTopButtons() {
        HBox topButtons = menuView.createTopButtons();
        topButtons.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(topButtons, new Insets(10, 0, 10, 0));
        return topButtons;
    }

    /**
     * Private method creating the buttons at the bottom of the board : undo, redo and refresh
     * @return the buttons contained in a HBox
     */
    private HBox createBottomButtons() {
        HBox bottomButtons = menuView.createBottomButtons();
        bottomButtons.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setMargin(bottomButtons, new Insets(10, 0, 10, 0));
        return bottomButtons;
    }

    /**
     * Removes all highlights from the board cells.
     */
    public void removeAllHighlights() {
        this.boardView.removeAllHighlights();
    }

    /**
     * Gets the CellViewFx at the specified row and column.
     * @param row The row of the cell
     * @param col The column of the cell
     * @return The CellViewFx at the specified position
     */
    public CellViewFx getCellAt(int row, int col) {
        return this.boardView.getCellView(row, col);
    }

    /**
     * Displays the winner or draw message.
     * @param winner The winning player, or null if it's a draw
     * @param game The current game instance
     */
    private void showWinner(Player winner, Game game) {
        String message;
        if (winner != null) {
            message = "Le gagnant est : " + (winner == game.getPink() ? "Joueur" : "Ordinateur");
        } else {
            message = "Le match est nul, aucun gagnant !";
        }
        updateMessage(message);
    }

    /**
     * Updates the message history with a new message.
     * @param message The new message to be added
     */
    public void updateMessage(String message) {
        messageView.updateMessage(message);
    }

    /**
     * Sets the disabled state and grayed appearance of the board.
     * @param disabled True to disable and gray out the board, false otherwise
     */
    private void setDisabledAndGrayed(boolean disabled) {
        this.boardView.setDisable(disabled);
        this.boardView.grayedBoard(disabled);
    }

    /**
     * Sets the action for the new game button.
     * @param handler The event handler for the new game action
     */
    public void setNewGameAction(EventHandler<ActionEvent> handler) {
        menuView.setNewGameAction(handler);
        messageView.resetHistory();
    }

    /**
     * Sets the action for the surrender button.
     * @param handler The event handler for the surrender action
     */
    public void setSurrenderAction(EventHandler<ActionEvent> handler) {
        menuView.setSurrenderAction(handler);
    }

    /**
     * Sets the action for the undo button.
     * @param handler The event handler for the undo action
     */
    public void setUndoAction(EventHandler<ActionEvent> handler) {
        menuView.setUndoAction(handler);
    }

    /**
     * Sets the action for the redo button.
     * @param handler The event handler for the redo action
     */
    public void setRedoAction(EventHandler<ActionEvent> handler) {
        menuView.setRedoAction(handler);
    }

    /**
     * Sets the action for the refresh button.
     * @param handler The event handler for the refresh action
     */
    public void setRefreshAction(EventHandler<ActionEvent> handler) {
        menuView.setRefreshAction(handler);
        messageView.resetHistory();
    }

    /**
     * Sets the action for the music toggle button.
     * @param handler The event handler for the music toggle action
     */
    public void setMusicAction(EventHandler<ActionEvent> handler) {
        menuView.setMusicAction(handler);
    }

    /**
     * Enables the refresh button.
     */
    private void showRefreshButton() {
        menuView.showRefreshButton();
    }

    /**
     * Disables the refresh button.
     */
    private void hideRefreshButton() {
        menuView.hideRefreshButton();
    }

    /**
     * Enables the undo and redo buttons.
     */
    private void showUndoRedoButtons() {
        menuView.showUndoRedoButtons();
    }

    /**
     * Disables the undo and redo buttons.
     */
    private void hideUndoRedoButtons() {
        menuView.hideUndoRedoButtons();
    }

    /**
     * Enables the surrender button.
     */
    private void showSurrenderButton() {
        menuView.showSurrenderButton();
    }

    /**
     * Disables the surrender button.
     */
    private void hideSurrenderButton() {
        menuView.hideSurrenderButton();
    }

    /**
     * Method updating the game information if the game state is at SURRENDER
     */
    private void updateSurrender() {
        Player winner = game.surrenderFX();
        this.showWinner(winner, game);
        this.showRefreshButton();
        this.hideUndoRedoButtons();
        this.hideSurrenderButton();
        this.setDisabledAndGrayed(true);

    }

    /**
     * Method updating the game information if the game state is at WIN
     */
    private void updateWin() {
        this.showWinner(game.getToPlay(), game);
        this.setDisabledAndGrayed(true);
        this.hideUndoRedoButtons();
        this.hideSurrenderButton();
        this.showRefreshButton();
    }

    /**
     * Method updating the game information if the game state is at DRAW
     */
    private void updateDraw() {
        this.showWinner(null, game);
        this.setDisabledAndGrayed(true);
        this.hideUndoRedoButtons();
        this.hideSurrenderButton();
        this.showRefreshButton();
    }

    /**
     * Method updating the game information if the game state is at REFRESH
     */
    private void updateRefresh() {
        this.setDisabledAndGrayed(false);
        this.hideRefreshButton();
        this.showUndoRedoButtons();
        this.showSurrenderButton();
        game.revengeGame();
        messageView.resetHistory();
    }

    /**
     * Method updating the game information if the game state is at MOVE or INSERT
     */
    private void updateMoveInsert(Game game) {
        int newSize = game.getBoardSize();
        if (boardView == null || newSize != boardView.getBoardSize()) {
            boardView = new BoardView(newSize);
            getChildren().set(3, setUpBoard());
        }
        boardView.update(game);
        infoView.update(game);
        this.removeAllHighlights();
        this.hideRefreshButton();
        this.showUndoRedoButtons();
        this.showSurrenderButton();
        this.setDisabledAndGrayed(false);
    }

    /**
     * Method updating the game information if the game state is at WIN or DRAW
     */
    private void updateWinDraw(Game game) {
        if (game.win()) {
            if (this.game.getGameState() == GameState.WIN) {
                updateWin();
                boardView.update(game);
            }
        }
        else if (game.isDraw()) {
            if (this.game.getGameState() == GameState.DRAW) {
                updateDraw();
            }
        }
    }

    /**
     * Updates the view based on the current game state.
     * @param game The current Game instance
     */
    public void updateObs(Game game) {
        if (!game.equals(this.game)) {
            this.game = game;
        }
        if (this.game.getGameState() == GameState.SURRENDER) {
            updateSurrender();
        }
        else if (this.game.getGameState() == GameState.REFRESH) {
            updateRefresh();
        }
        else if (this.game.getGameState() == GameState.MOVE || this.game.getGameState() == GameState.INSERT) {
            updateMoveInsert(game);
            updateWinDraw(game);
        }
    }
}
