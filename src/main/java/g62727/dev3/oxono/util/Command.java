package g62727.dev3.oxono.util;

/**
 * Interface writing a contract for a game command (insert a token, move a totem...)
 */
public interface Command {
    /**
     * Execute a command
     * Also used by redo
     */
    void execute();

    /**
     * Unexecute a command previously executed
     * Mainly used by undo
     */
    void unexecute();
}
