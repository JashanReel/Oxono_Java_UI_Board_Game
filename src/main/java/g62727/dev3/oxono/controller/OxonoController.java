package g62727.dev3.oxono.controller;

import g62727.dev3.oxono.util.Observer;
import g62727.dev3.oxono.view.CellViewFx;
import g62727.dev3.oxono.view.GameView;
import g62727.dev3.oxono.model.*;
import g62727.dev3.oxono.util.Strategy;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Controller used by JavaFX for the UI version of the game and also managing the game context/evolution
 * This class implements the Observer interface to handle updates from the game model.
 */

public class OxonoController implements Observer {
    /**
     * Attributes of the OxonoController class
     */
    private Game game;
    private GameView view;
    private Clip backGroundMusic;
    private boolean isMusicPlaying;

    /**
     * Initializes a new OxonoController with the given view
     * @param view -  The GameView instance to be controlled
     */
    public OxonoController(GameView view) {
        this.game = new Game(6);
        this.view = view;
    }

    /**
     * Initializes view listeners for various game actions and buttons
     */
    private void initializeViewListeners() {
        view.setNewGameAction(e -> startNewGame());
        view.setSurrenderAction(e -> surrender());
        view.setUndoAction(e -> handleUndo());
        view.setRedoAction(e -> handleRedo());
        view.setRefreshAction(e -> handleRefresh());
        view.setMusicAction(e -> toggleMusic());
    }

    /**
     * Toggles the background music on and off.
     */
    private void toggleMusic() {
        if (isMusicPlaying) {
            backGroundMusic.stop();
            view.updateMessage("Musique arrêtée");
        } else {
            backGroundMusic.start();
            view.updateMessage("Musique relancée");
        }
        isMusicPlaying = !isMusicPlaying;
    }

    /**
     * Updates the background music, loading and preparing the audio file.
     */
    private void updateMusic() {
        String path = "/Radagon.wav";
        InputStream is = getClass().getResourceAsStream(path);
        try (BufferedInputStream bis = new BufferedInputStream(is);
             AudioInputStream ais = AudioSystem.getAudioInputStream(bis)) {
            this.backGroundMusic = AudioSystem.getClip();
            backGroundMusic.open(ais);
            backGroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backGroundMusic.stop();
            isMusicPlaying = false;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la lecture de l'audio", e);
        }
    }

    /**
     * Starts a new game, prompting the user for game configuration.
     */
    public void startNewGame() {
        if (backGroundMusic != null) {
            backGroundMusic.stop();
            isMusicPlaying = false;
        }
        Dialog<Pair<String, String>> dialog = createConfigDialog();
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(this::setupNewGame);
        updateMusic();
    }

    /**
     * Creates and returns a dialog for configuring a new game.
     * @return A Dialog instance for game configuration
     */
    private Dialog<Pair<String, String>> createConfigDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle partie");
        dialog.setHeaderText("Configurez la nouvelle partie");
        ButtonType playButtonType = new ButtonType("Jouer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(playButtonType, ButtonType.CANCEL);
        TextField boardSize = new TextField("6");
        ComboBox<String> aiLevel = new ComboBox<>();
        aiLevel.getItems().addAll("Facile", "Moyen", "Difficile");
        aiLevel.setValue("Facile");
        GridPane grid = createConfigGrid(boardSize, aiLevel);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton ->
                dialogButton == playButtonType ? new Pair<>(boardSize.getText(), aiLevel.getValue()) : null);
        return dialog;
    }

    /**
     * Private method setting up the presentation of the dialog to configure the different game settings
     * @param boardSize - the input for the size of the board
     * @param aiLevel - the choice between the different AI levels
     * @return the grid presenting the form
     */
    private GridPane createConfigGrid(TextField boardSize, ComboBox<String> aiLevel) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.addRow(0, new Label("Taille du plateau:"), boardSize);
        grid.addRow(1, new Label("Niveau de l'adversaire:"), aiLevel);
        return grid;
    }

    /**
     * Sets up a new game based on the user's configuration choices.
     * @param pair A Pair containing the board size and AI difficulty level
     */
    private void setupNewGame(Pair<String, String> pair) {
        int size = Integer.parseInt(pair.getKey());
        String level = pair.getValue();
        this.game = new Game(size);
        this.game.setComputerStrategy(getStrategyForLevel(level));
        this.game.register(this);
        initializeViewListeners();
        for (int row = 0; row < game.getBoardSize(); row++) {
            for (int col = 0; col < game.getBoardSize(); col++) {
                view.getCellAt(row, col).setOnMouseClicked(this::handleCellClick);
            }
        }
    }

    /**
     * Returns a Strategy based on the selected difficulty level.
     * @param level The difficulty level as a String
     * @return A Strategy instance corresponding to the difficulty level
     */
    private Strategy getStrategyForLevel(String level) {
        return switch (level) {
            case "Facile" -> this.game.createRandomStrategy();
            case "Moyen" -> this.game.createWinPossibleStrategy();
            case "Diffcile" -> this.game.createMiniMaxStrategyDepth6();
            default -> new RandomStrategy();
        };
    }

    /**
     * Handles the surrender action in the game.
     */
    private void surrender() {
        this.game.surrender();
    }

    /**
     * Handles the refresh action in the game.
     */
    private void handleRefresh() {
        this.game.refresh();
    }

    /**
     * Handles cell clicks on the game board, managing totem selection, movement, and token insertion.
     * @param e The MouseEvent triggered by clicking a cell
     */
    private void handleCellClick(MouseEvent e) {
        CellViewFx cellView = (CellViewFx) e.getSource();
        int row = GridPane.getRowIndex(cellView), col = GridPane.getColumnIndex(cellView);
        if (game.getToInsert() == null) {
            handleTotemSelection(row, col);
        } else if (game.getGameState() == GameState.MOVE) {
            handleTotemMove(row, col);
        } else if (game.getGameState() == GameState.INSERT) {
            handleTokenInsert(row, col);
        }
    }

    /**
     * Handles the selection of a totem on the game board.
     * @param row The row of the selected cell
     * @param col The column of the selected cell
     */
    private void handleTotemSelection(int row, int col) {
        Pawn pawn = game.getPawnAt(new Position(row, col));
        if (pawn instanceof Totem) {
            Symbol symbol = pawn.getSymbol();
            if (game.enoughToMoveTotem(symbol)) {
                highlightValidMoves((Totem) pawn);
                this.view.updateMessage("Le totem " + symbol + " a été choisi");
            } else {
                showInsufficientTokensAlert(symbol);
            }
        }
    }

    /**
     * Displays an alert when there are insufficient tokens to move a totem.
     * @param symbol The Symbol of the totem that cannot be moved
     */
    private void showInsufficientTokensAlert(Symbol symbol) {
        this.view.updateMessage("Vous n'avez plus assez de jetons " + symbol);
    }

    /**
     * Handles the movement of a totem on the game board.
     * @param row The target row for the totem movement
     * @param col The target column for the totem movement
     */
    private void handleTotemMove(int row, int col) {
        Totem selectedTotem = game.findTotem(game.getToInsert());
        if (selectedTotem != null && game.moveTotem(selectedTotem, row, col)) {
            this.view.updateMessage("Totem " + selectedTotem.getSymbol() + " move avec succès !");
            highlightValidTokenInsertions();
        } else {
            this.view.removeAllHighlights();
            this.view.updateMessage("Mouvement invalide !");
        }
    }

    /**
     * Handles the insertion of a token on the game board.
     * @param row - The row where the token is to be inserted
     * @param col - The column where the token is to be inserted
     */
    private void handleTokenInsert(int row, int col) {
        boolean inserted = game.insertToken(row, col);
        if (inserted) {
            insertSound();
            handleInsertTrue();
        }
        if (!inserted) {
            handleInsertFalse();
        }
        view.removeAllHighlights();
    }

    /**
     * Private method handling the SFX sound when inserting a token on the board
     */
    private void insertSound() {
        String path = "/OxonoInsertPiece.wav";
        InputStream is = getClass().getResourceAsStream(path);
        try (BufferedInputStream bis = new BufferedInputStream(is);
             AudioInputStream ais = AudioSystem.getAudioInputStream(bis)) {
            Clip insertSound = AudioSystem.getClip();
            insertSound.open(ais);
            FloatControl gainControl = (FloatControl) insertSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(+6.0f);
            insertSound.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles actions after a successful token insertion.
     */
    private void handleInsertTrue() {
        this.view.updateMessage("Jeton inséré avec succès !");
        boolean cpuPlayed = playComputerTurn();
        if (cpuPlayed) {
            this.view.updateMessage("L'ordinateur a joué !");
        }
    }

    /**
     * Handles actions after a failed token insertion.
     */
    private void handleInsertFalse() {
        game.undoToken();
        this.view.updateMessage("Échec de l'insertion");
    }

    /**
     * Executes the computer's turn in the game.
     * @return true if the computer successfully played a turn, false otherwise
     */
    private boolean playComputerTurn() {
        boolean cpuPlayed = game.computerTurnFx();
        view.removeAllHighlights();
        return cpuPlayed;
    }

    /**
     * Highlights valid moves for a selected totem on the game board.
     * @param totem The Totem for which to highlight valid moves
     */
    private void highlightValidMoves(Totem totem) {
        for (int row = 0; row < game.getBoardSize(); row++) {
            for (int col = 0; col < game.getBoardSize(); col++) {
                CellViewFx cellView = view.getCellAt(row, col);
                Position position = new Position(row, col);
                boolean isValid = game.isValidMove(totem, position);
                cellView.highlight(isValid);
            }
        }
    }

    /**
     * Highlights valid positions for token insertion on the game board.
     */
    private void highlightValidTokenInsertions() {
        for (int row = 0; row < game.getBoardSize(); row++) {
            for (int col = 0; col < game.getBoardSize(); col++) {
                CellViewFx cellView = view.getCellAt(row, col);
                Position position = new Position(row, col);
                boolean isValid = game.isValidInsert(position);
                cellView.highlight(isValid);
            }
        }
    }

    /**
     * Handles the undo action in the game.
     */
    private void handleUndo() {
        undoHelper(this.game.getGameState());
    }

    /**
     * Helper method to handle undo actions based on the current game state.
     * @param gameState The current GameState
     */
    private void undoHelper(GameState gameState) {
        if (gameState == GameState.MOVE) {
            undoStateMove();
        } else if (gameState == GameState.INSERT) {
            undoStateInsert();
        }
        this.view.removeAllHighlights();
    }

    /**
     * Handles undo action when the game state is MOVE.
     */
    private void undoStateMove() {
        if (game.undoTotem()) {
            this.view.updateMessage("Undo totem effectué !");
        } else {
            this.view.updateMessage("Il n'y a rien à défaire !");
        }
    }

    /**
     * Handles undo action when the game state is INSERT.
     */
    private void undoStateInsert() {
        if (game.undoToken()) {
            this.view.updateMessage("Undo token effectué !");
        } else {
            this.view.updateMessage("Il n'y a rien à défaire !");
        }
    }

    /**
     * Handles the redo action in the game.
     */
    private void handleRedo() {
        redoHelper(this.game.getGameState());
    }

    /**
     * Helper method to handle redo actions based on the current game state.
     * @param gameState The current GameState
     */
    private void redoHelper(GameState gameState) {
        if (gameState == GameState.MOVE) {
            redoHelperMove();
        } else if (gameState == GameState.INSERT) {
            redoHelperInsert();
        }
    }

    /**
     * Handles redo action when the game state is MOVE.
     */
    private void redoHelperMove() {
        if (game.redoTotem()) {
            this.view.updateMessage("Redo totem effectué !");
            Symbol lastMovedSymbol = game.getLastMovedSymbol();
            if (lastMovedSymbol != null) {
                highlightValidTokenInsertions();
            }
        } else {
            this.view.updateMessage("Il n'y a rien à refaire !");
        }
    }

    /**
     * Handles redo action when the game state is INSERT.
     */
    private void redoHelperInsert() {
        if (game.redoToken()) {
            this.view.updateMessage("Redo token effectué !");
            this.view.removeAllHighlights();
        } else {
            this.view.updateMessage("Il n'y a rien à refaire !");
        }
    }

    /**
     * Updates the view when notified of changes in the game model.
     */
    @Override
    public void updateObs() {
        this.view.updateObs(this.game);
    }
}
