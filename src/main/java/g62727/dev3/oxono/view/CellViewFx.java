package g62727.dev3.oxono.view;

import g62727.dev3.oxono.model.Pawn;
import g62727.dev3.oxono.model.Token;
import g62727.dev3.oxono.model.Totem;
import javafx.animation.RotateTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Represents every single cell of the game board
 * Empty, contains a token or a totem
 * Adapts to the symbol and color
 */
public class CellViewFx extends StackPane {
    /**
     * Attributes of the CellViewFx class
     */
    private Shape shape;
    private Text text;

    /**
     * Constructs a new CellViewFx with default styling.
     */
    CellViewFx() {
        setPrefSize(50, 50);
        String style = "-fx-background-color: rgb(136,68,148);" +
                "-fx-border-color: rgb(224, 58, 145);" +
                "-fx-border-width: 1px 1px 1px 1px;";
        setStyle(style);
        shape = new Circle(20);
        shape.setFill(Color.TRANSPARENT);
        text = new Text();
        text.setFont(Font.font(20));
        getChildren().addAll(shape, text);
    }

    /**
     * Updates the cell's appearance based on the given pawn.
     * @param pawn The pawn to be displayed in this cell
     */
    void update(Pawn pawn) {
        getChildren().clear();
        styleCellTotem(pawn);
        styleCellToken(pawn);
        styleCellNull(pawn);
    }

    /**
     * Styles the cell to display a totem.
     * @param pawn The pawn to be styled as a totem
     */
    private void styleCellTotem(Pawn pawn) {
        if (pawn instanceof Totem) {
            Rectangle shadowRectangle = shadowTotem();
            Rectangle totemRectangle = upperTotem();
            text.setText(pawn.getSymbol().toString());
            text.setTranslateY(-2);
            text.setFill(Color.WHITE);
            getChildren().addAll(shadowRectangle, totemRectangle, text);
        }
    }

    /**
     * Private method creating the lower darker rectangle to simulate a 3D pawn
     * @return the lower and darker rectangle composing a totem
     */
    private Rectangle shadowTotem() {
        Rectangle shadowRectangle = new Rectangle(40, 40);
        shadowRectangle.setArcWidth(15);
        shadowRectangle.setArcHeight(15);
        shadowRectangle.setFill(Color.rgb(31, 120, 156));
        shadowRectangle.setTranslateY(2);
        return shadowRectangle;
    }

    /**
     * Private method creating the upper lighter rectangle to simulate a 3D pawn
     * @return the upper and lighter rectangle composing a totem
     */
    private Rectangle upperTotem() {
        Rectangle totemRectangle = new Rectangle(38, 38);
        totemRectangle.setArcWidth(15);
        totemRectangle.setArcHeight(15);
        totemRectangle.setFill(Color.rgb(41, 160, 196));
        totemRectangle.setTranslateY(-1);
        return totemRectangle;
    }

    /**
     * Styles the cell to display a token.
     * @param pawn The pawn to be styled as a token
     */
    private void styleCellToken(Pawn pawn) {
        if (pawn instanceof Token t) {
            StackPane tokenPane = new StackPane();
            Circle shadowCircle = shadowCircle(t);
            Circle tokenCircle = upperCircle(t);
            tokenPane.getChildren().addAll(shadowCircle, tokenCircle, text);
            text.setText(pawn.getSymbol().toString());
            text.setFill(Color.WHITE);
            getChildren().addAll(tokenPane);
            animateTokens(t, shadowCircle, tokenCircle, tokenPane);
        }
    }

    /**
     * Private method creating the lower darker circle to simulate a 3D pawn
     * @return the lower and darker circle composing a token
     */
    private Circle shadowCircle(Token t) {
        Circle shadowCircle = new Circle(20);
        shadowCircle.setFill(t.getColor() == g62727.dev3.oxono.model.Color.PINK ?
                Color.rgb(170, 50, 120) : Color.rgb(30, 30, 30));
        shadowCircle.setTranslateY(2);
        return shadowCircle;
    }

    /**
     * Private method creating the upper lighter circle to simulate a 3D pawn
     * @return the upper and lighter circle composing a token
     */
    private Circle upperCircle(Token t) {
        Circle tokenCircle = new Circle(18);
        tokenCircle.setFill(t.getColor() == g62727.dev3.oxono.model.Color.PINK ?
                Color.rgb(219, 81, 178) : Color.rgb(50, 50, 50));
        return tokenCircle;
    }

    /**
     * Private method animating and rotating the 4 winning tokens
     * @param t - the token to potentially rotate and animate
     * @param shadow - the shadow circle to adapt the color of
     * @param light - the light circle to adapt the color of
     * @param tokenPane - the component to animate and rotate
     */
    private void animateTokens(Token t, Circle shadow, Circle light, StackPane tokenPane) {
        if (t.isWinning()) {
            light.setFill(Color.rgb(228, 195, 33));
            shadow.setFill(Color.rgb(200, 172, 32));
            RotateTransition RT = new RotateTransition(Duration.seconds(1), tokenPane);
            RT.setAxis(Rotate.Z_AXIS);
            RT.setFromAngle(0);
            RT.setToAngle(360);
            RT.setCycleCount(3);
            RT.play();
        }
    }

    /**
     * Styles the cell when it's empty.
     * @param pawn The pawn (null in this case) to indicate an empty cell
     */
    private void styleCellNull(Pawn pawn) {
        if (pawn == null) {
            shape = new Circle(20, Color.TRANSPARENT);
            text.setText("");
            getChildren().addAll(shape, text);
        }
    }

    /**
     * Resets the cell's style to its default appearance.
     */
    void resetStyle() {
        String style = "-fx-background-color: rgb(136,68,148);" +
                "-fx-border-color: rgb(224, 58, 145);" +
                "-fx-border-width: 1px 1px 1px 1px;";
        setStyle(style);
    }

    /**
     * Highlights the cell based on whether it's a valid move.
     * @param isValid True if the cell represents a valid move, false otherwise
     */
    public void highlight(boolean isValid) {
        setStyle("-fx-background-color: " + (isValid ? "rgb(161, 47, 106)" : "rgb(136,68,148)") + "; -fx-border-color: rgb(224, 58, 145);");
    }

    /**
     * Removes the highlight from the cell.
     */
    void removeHighlight() {
        setStyle("-fx-background-color: rgb(136,68,148); -fx-border-color: rgb(224, 58, 145);");
    }
}
