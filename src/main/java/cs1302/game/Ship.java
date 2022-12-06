package cs1302.game;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

/**
 * An animated object representing the player's ship.
 */
public class Ship extends AnimatedObject {

    /** Ship with engines on */
    private static final ImagePattern SHIP_ON = new ImagePattern(
            new Image("file:resources/game/ship_r_on.png"));
    /** Ship with engines off */
    private static final ImagePattern SHIP_OFF = new ImagePattern(
            new Image("file:resources/game/ship_r.png"));

    private static final int SHIP_WIDTH = 30;
    private static final int SHIP_HEIGHT = 30;

    private static final int FIRE_DELAY = 60;

    private boolean enginesOn;

    /**
     * Creates a new Ship with the specified Game.
     * 
     * @param game
     * @throws NullPointerException if the game is null
     */
    public Ship(Game game) {
        super(game);
        // this polygon covers roughly the same area as the visible pixels of the
        // ship image
        Polygon shipShape = new Polygon();
        shipShape.getPoints().addAll(0.0, 0.0, //
                12.0, 0.0, //
                21.0, 4.0, //
                30.0, 13.0, //
                30.0, 15.0, //
                21.0, 26.0, //
                12.0, 30.0, //
                0.0, 30.0, //
                0.0, 0.0);
        shape = shipShape;
        shape.setFill(SHIP_OFF);
        setMaxSpeed(10);
        enginesOn = false;
    }

    /**
     * Turns the ship's engines on or off depending on the specified value.
     * 
     * @param value {@code true} if the engines should be turned on, false otherwise
     */
    public void setEnginesOn(boolean value) {
        enginesOn = value;
    }

    /**
     * Returns whether the ship's engines are on or not.
     * 
     * @return @return {@code true} if the ship's engines are on; otherwise
     *         {@code false}
     */
    public boolean isEnginesOn() {
        return enginesOn;
    }

    @Override
    public void update() {
        if (enginesOn) {
            changeVelocity(getDirection().multiply(0.1));
            shape.setFill(SHIP_ON);
        } else {
            shape.setFill(SHIP_OFF);
        }

        // rotation
        updateDirection();
        // position
        updatePosition();
    }

}
