package cs1302.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class representing a projectile. Projectiles have a limited display time, and
 * they should disappear from the screen if {@link #getTimeLeft()} returns 0.
 * 
 */
public class Projectile extends AnimatedObject {

    /** Time left from display time */
    private int timeLeft;

    /**
     * Creates a new Projectile with the specified Game end display time. Negative
     * display time is treated as ZERO(no display time).
     * 
     * @param game        the game containing this projectile
     * @param displayTime the display time
     * @throws NullPointerException if game is null
     */
    public Projectile(Game game, int displayTime) {
        super(game);
        shape = new Rectangle(4, 4);
        shape.setFill(Color.AQUAMARINE);
        timeLeft = Math.max(0, displayTime);
    }

    /**
     * Returns the time left from this projectile's display time.
     * 
     * @return the time left
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    @Override
    public void update() {
        updateDirection();
        updatePosition();
        timeLeft = Math.max(0, timeLeft - 1);
    }

}
