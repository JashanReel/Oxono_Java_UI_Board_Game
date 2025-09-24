package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * The facade of the application
 * Represents and manages each game
 * Through the commands, knows how/when to move a totem if the action is possible, same for inserting a token
 * Makes the CPU play
 * Undoes/Redoes actions
 * Is observable so the views will adapt automatically
 */
public class Game implements Observable {
    /**
     * Attributes of the Game class
     * The black player represents the computer
     * The pink player represents the human
     */
    private List<Observer> observers;
    private boolean running;
    private CommandManager commandManager;
    private Oxono oxono;

    /**
     * Constructor to instantiate a new game
     * Initialize the board, players, currentPlayer and the computer's context
     */
    public Game(int size) {
        this.oxono = new Oxono(size);
        this.running = true;
        this.commandManager = new CommandManager();
        this.observers = new ArrayList<>();
    }

    /**
     * Getter returning the size of the length/width of the board
     * @return the size of the board
     */
    public int getBoardSize() {
        return oxono.getBoardSize();
    }

    /**
     * Getter returning the value of the player of the color parameter
     * @param color - the color of the player to get the score of
     * @return the value of the player's score
     */
    public int getScore(Color color) {
        return this.oxono.getScore(color);
    }

    /**
     * Method returning the symbol of the last executed command where we had to move a totem
     * @return this concerned symbol
     */
    public Symbol getLastMovedSymbol() {
        return this.oxono.getLastMovedSymbol();
    }

    /**
     * Setter modifying the value of the symbol of the most recently moved totem
     * @param lastMovedTotemSymbol - the new most recently symbol
     */
    void setLastMovedTotemSymbol(Symbol lastMovedTotemSymbol) {
        this.oxono.setLastMovedTotemSymbol(lastMovedTotemSymbol);
    }

    /**
     * Getter returning whether the game is running or not
     * @return true if it's the case, false otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Method determining whether an insert is correct or not
     * Used to highlight the correct insertions in the graphical interface
     * @param position - where the token should be inserted
     * @return true if it's possible, false otherwise
     */
    public boolean isValidInsert(Position position) {
        return this.oxono.isValidInsert(new Token(this.oxono.getToInsert(), this.oxono.getToPlay().getColor()), position);
    }

    /**
     * Method determining whether a move is correct or not
     * Used to highlight the correct moves in the graphical interface
     * @param totem - the totem to move
     * @param position - where the totem should be moved
     * @return true if it's possible, false otherwise
     */
    public boolean isValidMove(Totem totem, Position position) {
        return this.oxono.isValidMove(totem, position);
    }

    /**
     * Method setting the game state to Refresh in order to rematch the opponent on the exact same board size and AI level
     */
    public void refresh() {
        this.setGameState(GameState.REFRESH);
        this.notifyObservers();
    }

    /**
     * Method setting the game state to Surrender in order to let the other player win
     */
    public void surrender() {
        this.setGameState(GameState.SURRENDER);
        this.notifyObservers();
    }

    /**
     * Method making the non-abandoning player win by switching the players
     * @return the winner by forfeit
     */
    public Player surrenderFX() {
        Player winner = (this.oxono.getToPlay() == this.oxono.getPink()) ? oxono.getBlack() : oxono.getPink();
        this.oxono.incrementScore(winner);
        this.running = false;
        return winner;
    }

    /**
     * Method allowing access to the game context : move phase or insert phase
     * @return the correct state of the game
     */
    public GameState getGameState() {
        return this.oxono.getGameState();
    }

    /**
     * Method modifying the value of the curent game state
     * @param gameState - the new game state of the game
     */
    void setGameState(GameState gameState) {
        this.oxono.setGameState(gameState);
    }

    /**
     * Getter giving access to the current player
     *
     * @return - the current player
     */
    public Player getToPlay() {
        if (this.getGameState() == GameState.WIN ||
            this.getGameState() == GameState.DRAW ||
            this.getGameState() == GameState.SURRENDER || running) {
            return this.oxono.getToPlay();
        }
        return null;
    }

    /**
     * Getter giving access to the symbol to be inserted next
     *
     * @return - the symbol in question
     */
    public Symbol getToInsert() {
        if (this.getGameState() == GameState.WIN ||
                this.getGameState() == GameState.DRAW ||
                this.getGameState() == GameState.SURRENDER || running) {
            return this.oxono.getToInsert();
        }
        return null;
    }

    /**
     * Getter giving access to the black-computer player
     *
     * @return the black-computer player
     */
    public Player getBlack() {
        if (this.getGameState() == GameState.WIN ||
                this.getGameState() == GameState.DRAW ||
                this.getGameState() == GameState.SURRENDER || running) {
            return this.oxono.getBlack();
        }
        return null;
    }

    /**
     * Getter giving access to the pink-human player
     *
     * @return the pink - human player
     */
    public Player getPink() {
        if (this.getGameState() == GameState.WIN ||
                this.getGameState() == GameState.DRAW ||
                this.getGameState() == GameState.SURRENDER || running) {
            return this.oxono.getPink();
        }
        return null;
    }

    /**
     * Method checking if there's a winner
     *
     * @return true if it's the case, false otherwise
     */
    public boolean win() {
        boolean won = this.oxono.checkForWinner();
        if (won) {
            switchPlayer();
            Player winner = this.oxono.getToPlay();
            this.oxono.incrementScore(winner);
            this.running = false;
            this.setGameState(GameState.WIN);
        }
        return won;
    }

    /**
     * Method resetting the board after asking for a rematch against the AI
     * Replace the totems at the center and clears the board from any possible tokens
     * The score is incremented, the settings conserved (board size, AI's strategy, number of tokens)
     */
    public void revengeGame() {
        int humanScore = this.oxono.getScore(Color.PINK);
        int cpuScore = this.oxono.getScore(Color.BLACK);
        Strategies oldStrat = this.oxono.getLastStrat();
        this.oxono = new Oxono(this.oxono.getBoardSize());
        this.oxono.setPlayerScore(humanScore);
        this.oxono.setComputerScore(cpuScore);
        this.oxono.getBlack().setStrategy(createStrategy(oldStrat));
        this.oxono.setToInsert(null);
        this.running = true;
        this.commandManager = new CommandManager();
        this.notifyObservers();
    }


    /**
     * Method checking if the game is a draw
     *
     * @return true if it's the case, false otherwise
     */
    public boolean isDraw() {
        boolean draw = this.oxono.isFull() ||
                ((this.oxono.getBlack().getTokensO() == 0 && this.oxono.getBlack().getTokensX() == 0)
                        &&
                        (this.oxono.getPink().getTokensO() == 0 && this.oxono.getPink().getTokensX() == 0));
        if (draw) {
            this.running = false;
            this.setGameState(GameState.DRAW);
        }
        return draw;
    }

    /**
     * Method switching who's the current player
     */
    void switchPlayer() {
        this.oxono.setToPlay((this.oxono.getToPlay() == oxono.getPink()) ? oxono.getBlack() : oxono.getPink());
    }

    /**
     * Method checking if the current player has the capacity to move the selected totem
     *
     * @param symbol - the symbol of the totem
     * @return true if the move is possible, false otherwsie
     */

    public boolean enoughToMoveTotem(Symbol symbol) {
        if (symbol == Symbol.O) {
            if (this.oxono.getToPlay().getTokensO() > 0) {
                this.oxono.setToInsert(symbol);
                return true;
            }
        } else if (symbol == Symbol.X) {
            if (this.oxono.getToPlay().getTokensX() > 0) {
                this.oxono.setToInsert(symbol);
                return true;
            }
        }
        return false;
    }

    /**
     * Method finding a totem on the board according to the string representation of its symbol
     *
     * @param symbol - the symbol of the totem to find
     * @return the found Totem
     */
    public Totem findTotem(Symbol symbol) {
        return this.oxono.findTotem(symbol);
    }

    /**
     * Method trying to move a totem to a new given position
     *
     * @param totem - the totem to move
     * @param row   - the row of the new position
     * @param col   - the col of the new position
     * @return true if the move was successful, false otherwise
     */
    public boolean moveTotem(Totem totem, int row, int col) {
        Position position = new Position(row, col);
        if (this.oxono.getGameState() == GameState.MOVE && running) {
            if (this.oxono.isValidMove(totem, position)) {
                this.oxono.setToInsert(totem.getSymbol());
                MoveTotemCommand moveTotemCommand = this.whatCommandForTotem(totem, position);
                this.commandManager.doIt(moveTotemCommand);
                this.notifyObservers();
                return true;
            }
        }
        this.oxono.setToInsert(null);
        return false;
    }

    /**
     * Private helper method determining what command to create according to the symbol of the totem
     * @param totem - the totem to move
     * @param position - the position where to move the totem
     * @return the newly created command to move the correct totem
     */
    private MoveTotemCommand whatCommandForTotem(Totem totem, Position position) {
        if (totem.getSymbol() == Symbol.O) {
            return new MoveTotemCommand(this, this.oxono, totem, position, this.oxono.totem_O_Pos().getRow(), this.oxono.totem_O_Pos().getCol());
        } else if (totem.getSymbol() == Symbol.X) {
            return new MoveTotemCommand(this, this.oxono, totem, position, this.oxono.totem_X_Pos().getRow(), this.oxono.totem_X_Pos().getCol());
        }
        throw new IllegalArgumentException("Impossible to create a valid moveTotemCommand");
    }

    /**
     * Method trying to insert a token to a given position
     *
     * @param row   - the row to insert the token
     * @param col   - the col to insert the token
     * @return true if the insertion was successful, false otherwise
     */
    public boolean insertToken(int row, int col) {
        Position position = new Position(row, col);
        Token token = new Token(this.oxono.getToInsert(), this.oxono.getToPlay().getColor());
        if (this.oxono.getGameState() == GameState.INSERT && running) {
            if (this.oxono.isValidInsert(token, position)) {
                InsertTokenCommand insertTokenCommand = new InsertTokenCommand(this, this.oxono, token, position, this.oxono.getToPlay());
                this.commandManager.doIt(insertTokenCommand);
                this.notifyObservers();
                return true;
            }
        }
        this.oxono.setToInsert(null);
        return false;
    }

    /**
     * Method returning the pawn at a given position
     *
     * @param position - the position to retrieve a pawn from
     * @return the pawn at the given position
     */
    public Pawn getPawnAt(Position position) {
        return oxono.getPawnAt(position);
    }

    /**
     * Method returning the number of free cases left
     *
     * @return the number of free cases left on the board
     */
    public int freeCasesLeft() {
        return this.oxono.freeCasesLeft();
    }

    /**
     * Undo method used when the player is asked to choose the totem to move
     * Goes back to the human player previous turn so 4 turns back
     * Undoes the CPU's past turn token insert
     * Undoes the CPU's past turn totem move
     * Undoes the human player's 2 turns back token insert
     * Undoes the human player's 2 turns back totem move and stops there --> the human player has to choose a totem now
     *
     * @return true if the undo was successful, false otherwise
     */
    public boolean undoTotem() {
        if (this.commandManager.canUndo() && running) {
            this.commandManager.undo();
            this.commandManager.undo();
            this.commandManager.undo();
            this.commandManager.undo();
            this.notifyObservers();
            return true;
        }
        return false;
    }

    /**
     * Undo method used when the player is asked to insert a token
     * Goes back to the start of the turn when the human player had to choose the totem to move
     */
    public boolean undoToken() {
        if (this.commandManager.canUndo() && running) {
            this.commandManager.undo();
            this.notifyObservers();
            return true;
        }
        return false;
    }

    /**
     * Redo method used when the player is asked to move a totem
     * Goes forward to the human player's same turn when he had to insert a token
     * The moved totem is restored
     *
     * @return true if the redo was successful, false otherwise
     */
    public boolean redoTotem() {
        if (this.commandManager.canRedo() && running) {
            this.commandManager.redo();
            this.oxono.setToInsert(this.oxono.getLastMovedSymbol());
            this.notifyObservers();
            return true;
        }
        return false;
    }

    /**
     * Redo method used when the player is asked to insert a token
     * Goes forward to the human player's turn
     * Redoes the human player's token insert
     * Redoes the CPU's totem move
     * Redoes the CPU's token insert and stops there --> the human player now has to choose a totem to move
     *
     * @return true if the redo was successful, false otherwise
     */
    public boolean redoToken() {
        if (this.commandManager.canRedo() && running) {
            this.commandManager.redo();
            this.commandManager.redo();
            this.commandManager.redo();
            this.notifyObservers();
            return true;
        }
        return false;
    }

    /**
     * Method playing the CPU's turn according to the adopted strategy
     * @return true if the CPU has played, false otherwise
     */
    public boolean computerTurn() {
        if (this.oxono.getToPlay().getStrategy() != null && running) {
            Totem chosenTotem = this.oxono.getBlack().chooseTotem(this.oxono);
            chosenTotem = switchTotem(chosenTotem.getSymbol(), chosenTotem);
            this.oxono.setToInsert(chosenTotem.getSymbol());
            Position totemMove = this.oxono.getBlack().chooseTotemMove(oxono, chosenTotem);
            this.moveTotem(chosenTotem, totemMove.getRow(), totemMove.getCol());
            Position tokenInsert = this.oxono.getBlack().chooseTokenInsert(oxono, chosenTotem);
            this.insertToken(tokenInsert.getRow(), tokenInsert.getCol());
            this.notifyObservers();
            this.oxono.setToInsert(null);
            return true;
        }
        return false;
    }

    /**
     * Method playing the CPU's turn according to the adopted strategy
     * Useful only when the game is launched in its GUI version since the other one would not create a pause transition
     * @return true if the CPU has played, false otherwise
     */
    public boolean computerTurnFx() {
        if (this.oxono.getToPlay().getStrategy() != null && running) {
            Totem chosenTotem = this.oxono.getBlack().chooseTotem(this.oxono);
            chosenTotem = switchTotem(chosenTotem.getSymbol(), chosenTotem);
            this.oxono.setToInsert(chosenTotem.getSymbol());
            Position totemMove = this.oxono.getBlack().chooseTotemMove(oxono, chosenTotem);
            Totem finalChosenTotem = chosenTotem;
            animateAIplay(finalChosenTotem, totemMove);
            return true;
        }
        return false;
    }

    /**
     * Private method animating the move and insert played by the AI opponent player
     * @param finalChosenTotem - the totem to move for the AI
     * @param totemMove - the position to move the totem to
     */
    private void animateAIplay(Totem finalChosenTotem, Position totemMove) {
        PauseTransition pauseMove = new PauseTransition(Duration.seconds(0.25));
        pauseMove.setOnFinished(event -> {
            this.moveTotem(finalChosenTotem, totemMove.getRow(), totemMove.getCol());
            this.notifyObservers();
            Position tokenInsert = this.oxono.getBlack().chooseTokenInsert(oxono, finalChosenTotem);
            animateAIinsert(tokenInsert);
        });
        pauseMove.play();
    }

    /**
     * Private method animating the insertion of a token played by the AI
     * @param tokenInsert - the position where to insert the token
     */
    private void animateAIinsert(Position tokenInsert) {
        PauseTransition pauseInsert = new PauseTransition(Duration.seconds(0.25));
        pauseInsert.setOnFinished(e -> {
            this.insertToken(tokenInsert.getRow(), tokenInsert.getCol());
            this.notifyObservers();
            this.oxono.setToInsert(null);
        });
        pauseInsert.play();
    }

    /**
     * Private helper method switching the chosen totem by the AI to the other one if the AI doesn't have enough tokens left
     * @param symbol - the symbol of the chosen totem to check the number of tokens for
     * @param chosenTotem - the chosen totem by the AI initially
     * @return the new chosen totem for the AI
     */
    private Totem switchTotem(Symbol symbol, Totem chosenTotem) {
        if (symbol == Symbol.X) {
            if (this.oxono.getBlack().getTokensX() <= 0) {
                return findTotem(Symbol.O);
            }
        }
        if (symbol == Symbol.O) {
            if (this.oxono.getBlack().getTokensO() <= 0) {
                return findTotem(Symbol.X);
            }
        }
        return chosenTotem;
    }

    /**
     * Setter modifying the strategy to adopt by the CPU to play along with
     *
     * @param strategy - the strategy to adopt
     */
    public void setComputerStrategy(Strategy strategy) {
        this.oxono.getBlack().setStrategy(strategy);
    }

    /**
     * Method used to register a new observer to observe the observable model
     * @param o - the observer to register
     */
    @Override
    public void register(Observer o) {
        if (!this.observers.contains(o)) {
            this.observers.add(o);
        }
        notifyObservers();
    }

    /**
     * Method used to unregister an already registered observer of the observers list
     * @param o - the observer to remove
     */
    @Override
    public void unregister(Observer o) {
        this.observers.remove(o);
    }

    /**
     * Method notifying every single observer of potential updates to make according to the game changes
     */
    private void notifyObservers() {
        for (Observer o : this.observers) {
            o.updateObs();
        }
    }

    /**
     * Private method creating the strategy to assign according to the enum strategies in parameter
     * @param strategy - the correct strategy enum to create
     * @return the newly created strategy to assign
     */
    private Strategy createStrategy(Strategies strategy) {
        this.oxono.setLastStrat(strategy);
        return strategy.createStrategy(this, this.oxono);
    }

    /**
     * Method instantiating a new RandomStrategy assigned to the AI player
     * @return the newly creating random strategy
     */
    public Strategy createRandomStrategy() {
        return this.createStrategy(Strategies.RANDOM);
    }

    /**
     * Method instantiating a new WinPossibleStrategy assigned to the AI player
     * @return the newly creating win possible strategy
     */
    public Strategy createWinPossibleStrategy() {
        return this.createStrategy(Strategies.WIN_POSSIBLE);
    }

    /**
     * Method instantiating a new MiniMaxStrategy with depth of 6 assigned to the AI player
     * @return the newly creating minimax strategy
     */
    public Strategy createMiniMaxStrategyDepth6() {
        return this.createStrategy(Strategies.MINIMAX_DEPTH_6);
    }
}
