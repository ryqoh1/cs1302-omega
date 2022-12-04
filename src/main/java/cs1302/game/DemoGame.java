package cs1302.game;

import java.util.Random;
import java.util.logging.Level;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * An example of a simple game in JavaFX. The play can move the rectangle left/right
 * with the arrow keys or teleport the rectangle by clicking it!
 */
public class DemoGame extends Game {

    private Random rng;       // random number generator
    private Rectangle player; // some rectangle to represent the player
    private Image shipon = new Image("file:resources/game/ship_r_on.png");
    private Image shipoff = new Image("file:resources/game/ship_r.png");


    /**
     * Construct a {@code DemoGame} object.
     * @param width scene width
     * @param height scene height
     */
    public DemoGame(int width, int height) {
        super(width, height, 60);            // call parent constructor
        setLogLevel(Level.INFO);             // enable logging
        this.rng = new Random();             // random number generator
        this.player = new Rectangle(30, 30); // some rectangle to represent the player

    } // DemoGame

    /** {@inheritDoc} */
    @Override
    protected void init() {
        player.setFill(new ImagePattern(shipoff));
        // setup subgraph for this component
        getChildren().addAll(player);         // add to main container
        // setup player
        player.setX(0);
        player.setY(0);
    } // init

    /** {@inheritDoc} */
    @Override
    protected void update() {

        // update player position
        isKeyPressed( KeyCode.LEFT, () -> player.setX(player.getX() - 10.0));
        isKeyPressed(KeyCode.RIGHT, () -> player.setX(player.getX() + 10.0));

        // update player position
        isKeyPressed( KeyCode.UP, () -> player.setY(player.getY() - 10.0));
        isKeyPressed(KeyCode.DOWN, () -> player.setY(player.getY() + 10.0));

    } // update

    /**
     * Move the player rectangle to a random position.
     * @param event associated mouse event
     */
    private void handleClickPlayer(MouseEvent event) {
        logger.info(event.toString());
        player.setX(rng.nextDouble() * (getWidth() - player.getWidth()));
        player.setY(rng.nextDouble() * (getHeight() - player.getHeight()));
    } // handleClickPlayer

} // DemoGame
