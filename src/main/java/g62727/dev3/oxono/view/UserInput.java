package g62727.dev3.oxono.view;

import g62727.dev3.oxono.model.*;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for everything related to asking the user's inputs for the console version of the game
 */
public class UserInput {
    /**
     * Attribute of the UserInput class
     */
    private Game model;
    private static final Pattern VALID_INPUT_PATTERN = Pattern.compile("^(O|X|Q|REDO|UNDO)$",
                                                                                            Pattern.CASE_INSENSITIVE);
    private static final Pattern VALID_INPUT_PATTERN_TOKEN = Pattern.compile("^(T(OKEN)?|U(NDO)?|R(EDO)?)$",
                                                                                            Pattern.CASE_INSENSITIVE);

    /**
     * Constructor to instantiate a new UserInput in order to ask various inputs and information to the user
     *
     * @param model - the game facade to set the CPU strategy, move the totem, insert a token
     */
    public UserInput(Game model) {
        this.model = model;
    }

    /**
     * Method asking the user which strategy the CPU should use, hardens the game accordingly
     *
     * @param input - the Scanner asking the user their input
     */
    public void computerLevel(Scanner input) {
        int level;
        do {
            System.out.print("De quel niveau sera votre adversaire (1, 2, 3...) : ");
            level = this.readLevelInput(input);
        } while (!this.cpuLevelTry(level));
        this.setCPUlevel(level);
    }

    /**
     * Private helper method asking the user for their input and opponent's level
     * Catches an InputMismatchException if the user enters anything other than a number
     * @param input - the scanner asking the user for their input
     * @return the chosen number, 0 if the user didn't enter a number
     */
    private int readLevelInput(Scanner input) {
        String userInput = input.nextLine().trim();
        if (isValidSingleDigit(userInput)) {
            return Integer.parseInt(userInput);
        } else {
            System.out.println("Veuillez n'entrer qu'un seul chiffre et qu'il soit valide !");
            return 0;
        }
    }

    private boolean isValidSingleDigit(String input) {
        return Pattern.compile("^[0-9]$").matcher(input).matches();
    }

    /**
     * Private helper method determining whether the user has chosen a correct CPU level for their opponent
     * @param level - the level chosen by the user
     * @return true if the level is valid, false otherwise
     */
    private boolean cpuLevelTry(int level) {
        if (level == 0) {
            return false;
        }
        if (level == 1 || level == 2 || level == 3) {
            return true;
        } else {
            System.out.println("Ce niveau de difficulté n'existe pas encore malheureusement, choisissez-en un autre.");
            return false;
        }
    }

    /**
     * Private helper method determining the cpu's strategy according to the valid opponent level
     * @param level - the valid level for the strategy
     */
    private void setCPUlevel(int level) {
        if (level == 1) {
            this.model.setComputerStrategy(this.model.createRandomStrategy());
        }
        if (level == 2) {
            this.model.setComputerStrategy(this.model.createWinPossibleStrategy());
        }
        if (level == 3) {
            this.model.setComputerStrategy(this.model.createMiniMaxStrategyDepth6());
        }
    }

    /**
     * Method asking the user the coordinates where the totem/token should move/be inserted
     * Loops while the position isn't in the board's limits
     *
     * @param prompt - the context of the input
     * @param input  - the Scanner asking the user for their input
     * @return the chosen correct coordinates
     */
    private int[] getValidCoords(String prompt, Scanner input) {
        System.out.println(prompt);
        int row, col;
        do {
            row = readCoordRowOrCol(input, "ligne");
            col = readCoordRowOrCol(input, "colonne");
            if (row < 0 || row > model.getBoardSize() || col < 0 || col > model.getBoardSize())
                System.out.println("Coordonnées invalides. Veuillez réessayer.");
        } while (row < 0 || row > model.getBoardSize() || col < 0 || col > model.getBoardSize());
        return new int[]{row, col};
    }

    /**
     * Private helper method asking the user for their input for the row or column value
     * Catches an InputMismatchException if the user enters anything other than a number
     * @param input - the scanner asking the user for their input
     * @param rowColStr - the represented row/col to evaluate
     * @return the chosen number, -1 if the user didn't enter a number
     */
    private int readCoordRowOrCol(Scanner input, String rowColStr) {
        System.out.print("Entrez une " + rowColStr + " valide (0-" + (model.getBoardSize() - 1) + ") : ");
        String userInput = input.nextLine().trim();
        if (isValidCoord(userInput)) {
            return Integer.parseInt(userInput);
        } else {
            System.out.println("Veuillez n'entrer qu'un chiffre valide entre 0 et " + (model.getBoardSize() - 1) + " !");
            return -1;
        }
    }

    /**
     * Private helper method matching the user's input with the regex
     * The user has to enter a number between 0 and the board size
     * @param input - the user's input
     * @return true if it matches and the input's correct, false otherwise
     */
    private boolean isValidCoord(String input) {
        return Pattern.compile("^[0-" + (model.getBoardSize() - 1) + "]$").matcher(input).matches();
    }

    /**
     * Method asking the user which totem to move or whether they'd like to give up or undo/redo
     *
     * @param input - the Scanner asking the user for their input
     * @return the player's final decision
     */
    public String whichTotem(Scanner input) {
        System.out.print("Choisissez le totem à déplacer (O/X), ou undo/redo, ou q pour abandonner : ");
        String totem = input.nextLine().toUpperCase();
        while (!isValidInputTotem(totem)) {
            System.out.print("Vous n'avez pas entré une commande valide, réessayez : ");
            totem = input.nextLine().toUpperCase();
        }
        return totem;
    }

    /**
     * Private helper method using regex pattern and matcher in order to verify the player's command when moving a totem
     * @param input - the Scanner asking the user for their input
     * @return true if the player's command matches the regex, false otherwise
     */
    private boolean isValidInputTotem(String input) {
        Matcher matcher = VALID_INPUT_PATTERN.matcher(input);
        return matcher.matches();
    }

    /**
     * Method asking the player whether they'd like to insert a token or undo/redo
     *
     * @param input - the Scanner asking the user for their input
     * @return the player's final decision
     */
    public char whatNowToken(Scanner input) {
        while (true) {
            System.out.print("Souhaitez-vous placer un [T]oken ou [U]ndo/[R]edo ? : ");
            String decision = input.nextLine().trim();
            if (isValidInputToken(decision)) {
                return decision.toUpperCase().charAt(0);
            }
            System.out.println("Entrée invalide. Veuillez réessayer.");
        }
    }

    /**
     * Private helper method using regex pattern and matcher in order to verify the player's command when inserting a token
     * @param input - the Scanner asking the user for their input
     * @return true if the player's command matches the regex, false otherwise
     */
    private boolean isValidInputToken(String input) {
        Matcher matcher = VALID_INPUT_PATTERN_TOKEN.matcher(input);
        return matcher.matches();
    }

    /**
     * Method moving the chosen totem according to the string-symbol
     * Loops and keeps asking coordinates while they're not valid AND movables cases
     *
     * @param prompt      - the context of the input
     * @param input       - the Scanner asking the user for their input
     * @param totemSymbol - the symbol in string of the totem to move
     */
    public void moveTotem(String prompt, Scanner input, Symbol totemSymbol) {
        int[] rowCol;
        boolean done;
        do {
            rowCol = this.getValidCoords(prompt, input);
            done = this.model.moveTotem(this.model.findTotem(totemSymbol), rowCol[0], rowCol[1]);
            if (!done) {
                System.out.println("Vous ne pouvez pas déplacer le totem ici... Réessayez !");
            }
        } while (!done);
    }

    /**
     * Method inserting a token according to the player's current color AND the symbol of the chosen totem moved
     *
     * @param prompt - the context of the input
     * @param input  - the Scanner asking the user for their input
     */
    public void insertToken(String prompt, Scanner input) {
        System.out.println("Placez un jeton " + this.model.getToInsert() + " !");
        int[] rowCol;
        boolean done;
        do {
            rowCol = this.getValidCoords(prompt, input);
            done = this.model.insertToken(rowCol[0], rowCol[1]);
            if (!done) System.out.println("Vous ne pouvez pas placer votre jeton ici... Réessayez !");
        } while (!done);
    }
}
