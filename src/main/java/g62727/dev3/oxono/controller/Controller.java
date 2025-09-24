package g62727.dev3.oxono.controller;

import g62727.dev3.oxono.model.Game;
import g62727.dev3.oxono.model.Symbol;
import g62727.dev3.oxono.view.UserInput;
import g62727.dev3.oxono.view.View;

import java.util.Scanner;

/**
 * Controller used by the console game and managing the game context and evolution
 */
public class Controller {
    /**
     * Attributes of the Controller class
     */
    private View view;
    private UserInput userInput;
    private Game model;

    /**
     * Constructor to instantiate a new controller and start the game
     *
     * @param view      - the class from which to pick the correct game state/context messages for the user
     * @param userInput - the class from which to pick the correct input demands for the user
     * @param model     - the facade containing all the game logic and algorithms
     */
    public Controller(View view, UserInput userInput, Game model) {
        this.view = view;
        this.userInput = userInput;
        this.model = model;
    }

    /**
     * Method interacting with the user and running the game accordingly
     */
    public void start() {
        Scanner input = new Scanner(System.in);
        boolean game = true;
        this.userInput.computerLevel(input);
        while (game) {
            this.startOfLoop();
            game = this.playTurn(input);
            game = game && this.winOrDrawCheck();
        }
        this.endGameContext();
    }

    /**
     * Private helper method going through the turn of the human player
     * @param input - the Scanner asking the user for their input
     * @return false if the player surrenders, true otherwise
     */
    private boolean playTurn(Scanner input) {
        boolean cpuPlayed = this.model.computerTurn();
        if (!cpuPlayed) {
            String totem = this.userInput.whichTotem(input);
            if (totem.equals("Q")) {
                this.view.surrender();
                return false;
            }
            this.handlePlayerAction(totem, input);
        }
        return true;
    }

    /**
     * Private helper method going through the undo/redo/OX possibilities of the game
     * @param totem - the player's action during the totem phase of the game
     * @param input - the Scanner asking the user for their input
     */
    private void handlePlayerAction(String totem, Scanner input) {
        this.undoTotem(totem);
        this.redoTotem(totem, input);
        this.O_X_Totem(totem, input);
    }

    /**
     * Private method displaying the board at every start of turn and asking the player what to do
     */
    private void startOfLoop() {
        System.out.println("-".repeat(100));
        this.view.displayBoard();
        this.view.playTextMove();
    }

    /**
     * Method determining what to do if the player decides to play the O/X totem
     *
     * @param totem - the chosen totem to play with
     * @param input - asking the player for their input
     */
    private void O_X_Totem(String totem, Scanner input) {
        if (totem.equals("O") || totem.equals("X")) {
            if (!this.model.enoughToMoveTotem(
                    (totem.equalsIgnoreCase("o") ? Symbol.O : Symbol.X)
            )) {
                this.view.errorMsgNoMoreTokensLeft();
            } else {
                this.userInput.moveTotem("Choisissez une position pour déplacer le totem !", input,
                                                    (totem.equalsIgnoreCase("o") ? Symbol.O : Symbol.X));
                this.view.displayBoard();
                char decision = this.userInput.whatNowToken(input);
                this.redoOrOX_then_undo_redo_token(decision, input);
            }
        }
    }

    /**
     * Method determining what to do if the player decides to undo instead of playing a totem
     *
     * @param totem - the command given by the player
     */
    private void undoTotem(String totem) {
        if (totem.equalsIgnoreCase("UNDO")) {
            boolean didUndo = this.model.undoTotem();
            if (!didUndo) {
                this.view.errorMsgUndoImpossible();
            }
        }
    }

    /**
     * Method determining what to do if the player decides to redo instead of playing a totem
     *
     * @param totem - the command given by the player
     * @param input - asking the player for their input
     */
    private void redoTotem(String totem, Scanner input) {
        if (totem.equalsIgnoreCase("REDO")) {
            boolean didRedo = this.model.redoTotem();
            this.view.displayBoard();
            if (this.model.getToInsert() != null && didRedo) {
                char decision = this.userInput.whatNowToken(input);
                this.redoOrOX_then_undo_redo_token(decision, input);
            } else {
                this.view.errorMsgNoTotemChosen();
            }
        }
    }

    /**
     * Private helper method determining to ask to insert a token, undo or redo according to the user's decision
     * @param decision - the user's choice
     * @param input - the Scanner asking the user for their input
     */
    private void redoOrOX_then_undo_redo_token(char decision, Scanner input) {
        if (decision == 'T') {
            this.userInput.insertToken("Choisissez où le placer !", input);
        } else if (decision == 'U') {
            this.model.undoToken();
        } else if (decision == 'R') {
            this.redoToken();
        }
    }

    /**
     * Method determining what to do if the player decides to redo instead of inserting a token
     */
    private void redoToken() {
        boolean didRedoToken = this.model.redoToken();
        if (!didRedoToken) {
            this.view.errorMsgRedoImpossible();
            this.model.undoToken();
        }
    }

    /**
     * Method determining if there's a winner or a draw and updates the game loop accordingly
     *
     * @return true if there's nothing special and the game must go on, false otherwise if there's a winner/draw
     */
    private boolean winOrDrawCheck() {
        if (this.model.win()) {
            this.view.winText();
            return false;
        }
        if (this.model.isDraw()) {
            this.view.drawText();
            return false;
        }
        return true;
    }

    /**
     * Method displaying the final message and board at the end of a game
     */
    private void endGameContext() {
        this.view.displayBoard();
        System.out.println("Fin de partie ! Merci d'avoir joué !!!");
    }
}
