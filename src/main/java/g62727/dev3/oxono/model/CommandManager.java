package g62727.dev3.oxono.model;

import g62727.dev3.oxono.util.Command;

import java.util.Stack;

/**
 * Class creating two stacks for the normal, undo and redo commands, also checking if it's possible
 */
public class CommandManager {
    /**
     * Attributes of the CommandManager class
     */
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    /**
     * Constructor to instantiate the stacks used by undo/redo
     */
    CommandManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Method executing a command
     *
     * @param command - the command to execute
     */
    void doIt(Command command) {
        this.undoStack.add(command);
        command.execute();
        this.redoStack.clear();
    }

    /**
     * Method allowing the user to come back to an anterior state of the game
     */
    void undo() {
        if (!this.undoStack.empty()) {
            Command command = this.undoStack.pop();
            command.unexecute();
            this.redoStack.add(command);
        }
    }

    /**
     * Method allowing the user to go forward through the past states of the game
     */
    void redo() {
        if (!this.redoStack.empty()) {
            Command command = this.redoStack.pop();
            command.execute();
            this.undoStack.add(command);
        }
    }

    /**
     * Method informing the user whether the game can be undone or not
     *
     * @return false if it can't, true otherwise
     */
    boolean canUndo() {
        return !undoStack.empty();
    }

    /**
     * Method informing the user whether the game can be redone or not
     *
     * @return false if it can't, true otherwise
     */
    boolean canRedo() {
        return !redoStack.empty();
    }
}
