package g62727.dev3.oxono.view;

import g62727.dev3.oxono.model.*;

/**
 * Class responsible for the basic display messages
 * When a game is finished by a win or a draw or by surrendering
 * Displaying the board, error messages for undo/redo
 * Number of tokens left, number of empty cells left
 */
public class View {
    /**
     * Attribute of the View class
     */
    private Game model;
    private final String RESET = "\u001B[0m";
    private final String PINK = "\u001B[35m";
    private final String BLACK = "\u001B[30m";
    private final String BLUE = "\u001B[34m";

    /**
     * Constructor to instantiate a new view in order to display the correct game state/context messages
     *
     * @param model - the game facade helping the view determining who's the current player, giving access to prints...
     */
    public View(Game model) {
        this.model = model;
    }

    /**
     * Message displayed when the game is finished because one of the players won
     * Clarifies who's the winner and the loser
     */
    public void winText() {
        System.out.println("La partie est terminée !");
        System.out.println("La défaite tombe sur : " + ((this.model.getToPlay() == this.model.getPink()) ? "l'ordinateur !" : "vous..."));
        System.out.println("La victoire est remportée par : " + ((this.model.getToPlay() == this.model.getPink()) ? "vous !" : "l'ordinateur..."));
    }

    /**
     * Message displayed when the game is concluded by a draw between both of them
     */
    public void drawText() {
        System.out.println("La partie est terminée !");
        System.out.print("Match nul entre " + ((this.model.getToPlay() == this.model.getPink()) ? "vous" : "l'ordinateur"));
        System.out.println(" et " + ((this.model.getToPlay() == this.model.getPink()) ? "l'ordinateur !" : "vous !"));
    }

    /**
     * Message displayed when one of the player give up
     * Clarifies who won and who gave up
     */
    public void surrender() {
        System.out.println("Abandon de : " + ((this.model.getToPlay() == this.model.getPink()) ? "votre part..." : "la part de l'ordinateur !"));
        System.out.println("La victoire par abandon est remportée par : " +
                ((this.model.getToPlay() == this.model.getPink()) ? "l'ordinateur ..." : "vous !"));
    }

    /**
     * Message displayed when one of the player's turn begin
     * Clarifies the game context : how many tokens left, how many free cases left, whose turn it is now, who's playing
     */
    public void playTextMove() {
        System.out.println("C'est " + ((this.model.getToPlay() == this.model.getPink()) ? "à votre tour !" : "au tour de l'ordinateur !"));
        System.out.println("Voici le compte des jetons restants :");
        blackTokensLeft(this.model.getToPlay().getColor());
        pinkTokensLeft(this.model.getToPlay().getColor());
        this.getRemainingEmptyPawns();
        displayHuman_CPU(this.model.getToPlay());
    }

    /**
     * Helper method used to display the context action of the current player according to their type
     * @param player - the current player
     */
    private void displayHuman_CPU(Player player) {
        if (player == this.model.getPink()) {
            System.out.println("Déplacez un totem !");
        } else {
            System.out.println("L'ordinateur réfléchit...");
        }
    }

    /**
     * Helper method used to display the number of tokens left for the black player
     * @param color - the current player's color
     */
    private void blackTokensLeft(Color color) {
        if (color == Color.BLACK) {
            System.out.println("Jetons O : " + this.model.getBlack().getTokensO());
            System.out.println("Jetons X : " + this.model.getBlack().getTokensX());
        }
    }

    /**
     * Helper method used to display the number of tokens left for the pink player
     * @param color - the color of the current player
     */
    private void pinkTokensLeft(Color color) {
        if (color == Color.PINK) {
            System.out.println("Jetons O : " + this.model.getPink().getTokensO());
            System.out.println("Jetons X : " + this.model.getPink().getTokensX());
        }
    }

    /**
     * Method displaying the game board with colors thanks to the 4 precedent constants
     */
    public void displayBoard() {
        for (int row = 0; row < this.model.getBoardSize(); row++) {
            for (int col = 0; col < this.model.getBoardSize(); col++) {
                Pawn pawn = this.model.getPawnAt(new Position(row, col));
                nullPawn(pawn);
                notNullPawn(pawn);
            }
            System.out.println();
        }
    }

    /**
     * Helper method used to display an empty case when the pawn is null
     * @param pawn - the pawn to test
     */
    private void nullPawn(Pawn pawn) {
        if (pawn == null) {
            System.out.print(". ");
        }
    }

    /**
     * Helper method used to display a not null pawn
     * @param pawn - the pawn to display
     */
    private void notNullPawn(Pawn pawn) {
        if (pawn != null) {
            token_totem_color(pawn);
        }
    }

    /**
     * Helper method used to display the symbol according to the color of the token
     * @param pawn - the pawn to display
     */
    private void token_totem_color(Pawn pawn) {
        if (pawn instanceof Token) {
            Color color = ((Token) pawn).getColor();
            this.blackOrPink(color, pawn);
        } else {
            System.out.print(BLUE + pawn.getSymbol() + RESET + " ");
        }
    }

    /**
     * Method determining in which color to print a symbol depending on the pawn's symbol and color
     * @param color - the color of the pawn
     * @param pawn - the pawn to display
     */
    private void blackOrPink(Color color, Pawn pawn) {
        if (color == Color.BLACK) {
            System.out.print(BLACK + pawn.getSymbol() + RESET + " ");
        } else if (color == Color.PINK) {
            System.out.print(PINK + pawn.getSymbol() + RESET + " ");
        }
    }

    /**
     * Private method displaying the number of free cases left
     */
    private void getRemainingEmptyPawns() {
        System.out.println("Voici le nombre de cases vides restantes : " + this.model.freeCasesLeft());
    }

    /**
     * Method scolding the user when they try to redo but the redoStack is empty AND no totem si selected
     * Important method for the very first turn of the game
     */
    public void errorMsgNoTotemChosen() {
        System.out.println("-".repeat(70));
        System.out.println("Vous n'avez pas choisi de totem à déplacer, ne trichez pas !");
        System.out.println("-".repeat(70));
    }

    /**
     * Method scolding the user when they try to play a totem which has a symbol they no longer have any token for
     */
    public void errorMsgNoMoreTokensLeft() {
        System.out.println("-".repeat(70));
        System.out.println("Vous n'avez plus assez de jetons du même symbole pour jouer...");
        System.out.println("-".repeat(70));
    }

    /**
     * Method scolding the user when they try to undo an action in the MOVE gameState but the undoStack is empty
     */
    public void errorMsgUndoImpossible() {
        System.out.println("-".repeat(70));
        System.out.println("Il n'y a rien à défaire !");
        System.out.println("-".repeat(70));
    }

    /**
     * Method scolding the user when they try to redo an action in the INSERT gameState but the redoStack is empty
     */
    public void errorMsgRedoImpossible() {
        System.out.println("-".repeat(70));
        System.out.println("Il n'y a rien à refaire !");
        System.out.println("-".repeat(70));
    }
}
