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

    private static final int DEFAULT_WEAPON_COOLDOWN = 60;

    /** Whether the engines are on or not */
    private boolean enginesOn;
    /** Cooldown remaining until the weapon can be fired */
    private int cooldownRemaining;
    /** The weapon cooldown */
    private int weaponCooldown;

    /**
     * Creates a new Ship with the specified Game.
     * 
     * @param game he game containing this ship
     * @throws NullPointerException if the game is null
     */
    public Ship(Game game) {
        super(game);
        // this polygon covers roughly the same area as the visible pixels of the
        // ship image
        Polygon shipShape = new Polygon(0.0, 0.0, //
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
        // defaults 
        setMaxSpeed(5);
        enginesOn = false;
        weaponCooldown = DEFAULT_WEAPON_COOLDOWN;
        cooldownRemaining = DEFAULT_WEAPON_COOLDOWN;
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

    /**
     * Sets the weapon cooldown of this ship. Negative values are treated as zero(no
     * cooldown).
     * 
     * @param cooldown
     */
    public void setWeaponCooldown(int cooldown) {
        weaponCooldown = cooldown;
    }

    /**
     * Attempts to fire the ship's weapon and returns the result of the operation.
     * 
     * @return {@code true} if the weapon was successfully fired, {@code false} otherwise.
     */
    public boolean fire() {
        if (cooldownRemaining != 0) {
            return false;
        } else {
            cooldownRemaining = Math.max(0, weaponCooldown);
            return true;
        }
    }

    @Override
    public void update() {
        // apply thrust if the engines are on
        if (enginesOn) {
            changeVelocity(getDirection().multiply(0.05));
            shape.setFill(SHIP_ON);
        } else {
            shape.setFill(SHIP_OFF);
        }

        // rotation
        updateDirection();
        // position
        updatePosition();
        cooldownRemaining = Math.max(0, cooldownRemaining - 1);
    }

}
