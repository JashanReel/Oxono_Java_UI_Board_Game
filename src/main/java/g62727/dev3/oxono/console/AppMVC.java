package g62727.dev3.oxono.console;

import g62727.dev3.oxono.controller.Controller;
import g62727.dev3.oxono.model.Game;
import g62727.dev3.oxono.view.UserInput;
import g62727.dev3.oxono.view.View;

/**
 * Public class allowing the user to play the game in console
 */
public class AppMVC {
    public static void main(String[] args) {
        Game model = new Game(6);
        View view = new View(model);
        UserInput userInput = new UserInput(model);
        Controller controller = new Controller(view, userInput, model);
        controller.start();
    }
}
