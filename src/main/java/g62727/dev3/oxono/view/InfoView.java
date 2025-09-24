package g62727.dev3.oxono.view;

import g62727.dev3.oxono.model.Color;
import g62727.dev3.oxono.model.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

/**
 * Responsible for displaying the current game info :
 * Current Player, number of O/X tokens left for each player, number of remaining empty cells
 */
public class InfoView extends VBox {
    /**
     * Attributes of the InfoView class
     */
    private Label playerTurnLabel;
    private Label humanTokensLeftLabel;
    private Label cpuTokensLeftLabel;
    private Label emptySpacesLabel;
    private Label humanScoreLabel;
    private Label cpuScoreLabel;

    /**
     * Constructs a new InfoView with labels for various game information.
     */
    InfoView() {
        playerTurnLabel = new Label();
        humanTokensLeftLabel = new Label();
        cpuTokensLeftLabel = new Label();
        emptySpacesLabel = new Label();
        humanScoreLabel = new Label();
        cpuScoreLabel = new Label();
        createInfoContainer();
    }

    /**
     * Private method creating the top box containing all the game information and context
     */
    private void createInfoContainer() {
        setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: #333;");
        setPadding(new Insets(10));
        setSpacing(5);
        setAlignment(Pos.TOP_CENTER);
        Label infoTitleLabel = createStylizedLabelInfoTitle();
        List<Label> infoLabels = listInfoLabels();
        getChildren().addAll(infoTitleLabel);
        for (Label label : infoLabels) {
            getChildren().addAll(label);
        }
    }

    /**
     * Method creating the information label title
     * @return the stylized label
     */
    private Label createStylizedLabelInfoTitle() {
        Label infoTitleLabel = new Label("Informations sur les joueurs");
        infoTitleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 20px;");
        return infoTitleLabel;
    }

    /**
     * Method returning a list of every stylized label
     * @return the list of every stylized label
     */
    private List<Label> listInfoLabels() {
        return Arrays.asList(
                createStylizedLabelInfo(playerTurnLabel),
                createStylizedLabelInfo(humanTokensLeftLabel),
                createStylizedLabelInfo(cpuTokensLeftLabel),
                createStylizedLabelInfo(emptySpacesLabel),
                createStylizedLabelInfo(humanScoreLabel),
                createStylizedLabelInfo(cpuScoreLabel)
        );
    }

    /**
     * Method creating the information labels with style
     * @param label - the label to stylized
     * @return the stylized label
     */
    private Label createStylizedLabelInfo(Label label) {
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        return label;
    }

    /**
     * Updates all labels with current game information.
     * @param game The current Game instance
     */
    void update(Game game) {
        playerTurnLabel.setText("Tour de : " + (game.getToPlay() == game.getPink() ? "Joueur" : "Ordinateur"));
        humanTokensLeftLabel.setText("Joueur : O = " + game.getPink().getTokensO() + " | X = " + game.getPink().getTokensX());
        cpuTokensLeftLabel.setText("Ordinateur : O = " + game.getBlack().getTokensO() + " | X = " + game.getBlack().getTokensX());
        emptySpacesLabel.setText("Cases vides : " + game.freeCasesLeft());
        humanScoreLabel.setText("Joueur : " + game.getScore(Color.PINK));
        cpuScoreLabel.setText("Ordinateur : " + game.getScore(Color.BLACK));
    }
}
