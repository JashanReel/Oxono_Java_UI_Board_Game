package g62727.dev3.oxono.util;

import g62727.dev3.oxono.model.Position;
import g62727.dev3.oxono.model.Oxono;
import g62727.dev3.oxono.model.Totem;

/**
 * Interface writing the contract to create a strategy used by the CPU
 */
public interface Strategy {
    /**
     * Computer's algorithm deciding which totem to choose between O and X
     *
     * @param oxono - the board where the totems are
     * @return - the chosen totem
     */
    Totem chooseTotem(Oxono oxono);

    /**
     * Computer's algorithm choosing a position where the chosen totem should be moved within the list of possible moves
     *
     * @param oxono - the board where the action happens
     * @param totem - the totem to move
     * @return the chosen position where the chosen totem will move
     */
    Position chooseTotemMove(Oxono oxono, Totem totem);

    /**
     * Computer's algorithm choosing a position to insert a token
     *
     * @param oxono - the board where the action happens
     * @param totem - the totem where the token has to be inserted adjacently
     * @return the position where the token will be inserted
     */
    Position chooseTokenInsert(Oxono oxono, Totem totem);
}
