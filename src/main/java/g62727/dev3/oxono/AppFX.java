package g62727.dev3.oxono;

import g62727.dev3.oxono.controller.OxonoController;
import g62727.dev3.oxono.view.GameView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main JavaFX class allowing the user to launch the UI version of the game
 */
public class AppFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        int initialSize = 6;
        GameView view = new GameView(initialSize);
        OxonoController controller = new OxonoController(view);
        controller.startNewGame();

        Scene scene = new Scene(view, 800, 600);
        scene.getRoot().setStyle("-fx-background-color: rgb(0, 169, 165)");
        Image icon = new Image("file:src/main/resources/Oxono_Icon.jpg");
        primaryStage.getIcons().addAll(icon);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Oxono");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
