package cs1302.game;

import java.util.Objects;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

/**
 * An animated object on the game screen.
 * <p>
 * Animated objects have a constant {@code velocity} and {@code spin} applied
 * after each {@link #update()} call. Instant motions, {@code movement} and
 * {@code rotate} are applied and cleared in the next call to {@link #update()}.
 * If multiple instant motions of some type are specified between two
 * {@link #update()} call, only the result of a single, combined motion may
 * become visible.
 * <p>
 * A maximum magnitude(length) of the velocity vector may be specified by
 * setting {@code maxSpeed}. Instant {@code movement} is not affected by
 * {@code maxSpeed}.
 * <p>
 * Subclasses must initialize the {@code object} field and implement
 * {@link #update()}.
 */
public abstract class AnimatedObject {

    private static final Point2D ZERO_MOVEMENT = new Point2D(0, 0);
    public static final double DEFAULT_MAX_SPEED = 50;
    public static final double DEFAULT_WRAP_PERCENT = 50;

    /** The game containing this object */
    protected final Game game;
    /** The shape used to display this object */
    protected Shape shape;

    /** Movement applied at every update */
    protected Point2D velocity;
    /** Spin applied at every update */
    protected double spin;
    /** Movement applied once, at the next update */
    protected Point2D movement;
    /** Spin applied once, at the next update */
    protected double rotate;
    /** The direction this object is facing */
    protected double direction;
    /** The maximum magnitude of the velocity */
    private double maxSpeed;
    /**
     * In percentage. Objects should be wrapped around if they are out of the screen
     * by more than this amount
     */
    private double wrapAt;

    /**
     * Creates a new AnimatedObject with the specified {@link #Game}.
     * 
     * @param game the game containing this object
     * @throws NullPointerException if {@code game} is null
     */
    public AnimatedObject(Game game) {
        Objects.requireNonNull(game);
        this.game = game;
        velocity = ZERO_MOVEMENT;
        movement = ZERO_MOVEMENT;
        maxSpeed = DEFAULT_MAX_SPEED;
        wrapAt = DEFAULT_WRAP_PERCENT;
    }

    /**
     * Sets the velocity of this object to the specified value. If {@code maxSpeed}
     * is set, the velocity vector's magnitude(length) may be reduced to not exceed
     * this value.
     * 
     * @param velocity the new velocity
     * @throws NullPointerException if {@code velocity} is null
     */
    public void setVelocity(Point2D velocity) {
        Objects.requireNonNull(velocity);
        double speed = velocity.magnitude();
        double mult = 1;
        if (Math.abs(speed) > 10) {
            mult = 10 / Math.abs(speed);
        }
        this.velocity = velocity.multiply(mult);
    }

    /**
     * Returns the velocity of this object.
     * 
     * @return the velocity
     */
    public Point2D getVelocity() {
        return velocity;
    }

    /**
     * Changes the velocity of this object by the specified value. If
     * {@code maxSpeed} is set, the velocity vector's magnitude(length) may be
     * reduced to not exceed this value.
     * 
     * @param delta
     * @throws NullPointerException if {@code delta} is null
     */
    public void changeVelocity(Point2D delta) {
        Objects.requireNonNull(velocity);
        Point2D newVelocity = velocity.add(delta);
        double speed = newVelocity.magnitude();
        double mult = 1;
        if (Math.abs(speed) > 10) {
            mult = 10 / Math.abs(speed);
        }
        velocity = newVelocity.multiply(mult);
    }

    /**
     * Returns the spin of this object in degrees.
     * 
     * @return the spin
     */
    public double getSpin() {
        return spin;
    }

    /**
     * Sets the spin of this object to the specified value in degrees.
     * 
     * @param spinInDegrees the new spin
     */
    public void setSpin(double spinInDegrees) {
        this.spin = spinInDegrees;
    }

    /**
     * Changes the spin of this object by the specified value in degrees.
     * 
     * @param deltaInDegrees the delta
     */
    public void changeSpin(double deltaInDegrees) {
        spin += deltaInDegrees;
    }

    /**
     * Returns the maximum speed of this object. The default maximum speed is
     * {@link #DEFAULT_MAX_SPEED}.
     * 
     * @return the current max speed
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Sets the maximum speed of this object to the specified value.
     * 
     * @param maxSpeed the new max speed
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Sets the value this object should be wrapped around if
     * 
     * @param wrapAtPercent
     */
    public void setWrapAtPercent(double wrapAtPercent) {
        this.wrapAt = wrapAtPercent;
    }

    /**
     * Returns the Shape used to display this object.
     * 
     * @return the Shape object
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Return the direction this object is facing, in degrees.
     * 
     * @return the direction
     */
    public double getDirection() {
        return direction;
    }

    /**
     * Instantly rotates this object by the specified angle in degrees. The effects
     * of this method call will not become visible until the next time the
     * {@link #update} method is called.
     * 
     * @param angleInDegrees the angle in degrees
     */
    public void rotate(double angleInDegrees) {
        rotate += angleInDegrees;
    }

    /**
     * Instantly moves this object by the specified delta. The effects of this
     * method call does not become visible until the next time the {@link #update}
     * method is called.
     * 
     * @param delta the delta
     */
    public void move(Point2D delta) {
        movement = movement.add(delta);
    }

    /**
     * Wraps this object around if it would leave the game area otherwise.
     */
    private void wrap() {
        Bounds gameBounds = game.getGameBounds();
        Bounds objectBounds = shape.getBoundsInParent();
        Bounds localBounds = shape.getBoundsInLocal();
        // center coodinates relative to the game area
        double x = objectBounds.getCenterX();
        double y = objectBounds.getCenterY();
        // size of the Bounds box around the shape
        double w = localBounds.getWidth();
        double h = localBounds.getHeight();
        // wrapAt is the percent of the object that should remain visible before
        // wrapping it around
        double wBeforeWrap = w * (wrapAt / 100) / 2;
        double hBeforeWrap = h * (wrapAt / 100) / 2;
        // objects are wrapped around when they are out of the game area by more than
        // the above specified amount
        // objects appear on the other side, also slightly out of the game area
        // in order to make the transition more visually pleasing
        // horizontal wrapping
        if (x - wBeforeWrap > gameBounds.getMaxX()) {
            shape.relocate(-(w - wBeforeWrap), objectBounds.getMinY());
        } else if (x + wBeforeWrap < gameBounds.getMinX()) {
            shape.relocate(gameBounds.getMaxX() - wBeforeWrap, objectBounds.getMinY());
        }
        // vertical wrapping
        if (y - hBeforeWrap > gameBounds.getMaxY()) {
            shape.relocate(objectBounds.getMinX(), -(h - hBeforeWrap));
        } else if (y + hBeforeWrap < gameBounds.getMinY()) {
            shape.relocate(objectBounds.getMinX(), gameBounds.getMaxY() - hBeforeWrap);
        }
    }

    /**
     * Updates the direction this object is facing.
     */
    protected void updateDirection() {
        direction = direction + rotate + spin;
        // keep direction in the [-180, 180] range
        if (direction > 180) {
            direction = -(360 - direction);
        }
        if (direction < -180) {
            direction = 360 + direction;
        }
        // clear instant rotation
        rotate = 0;
        shape.setRotate(direction);
    }

    /**
     * Updates the position of this object, wrapping if neccessary.
     */
    protected void updatePosition() {
        Point2D delta = movement.add(velocity);
        // clear instant movement
        movement = ZERO_MOVEMENT;
        // set new position
        double x = shape.getTranslateX();
        double y = shape.getTranslateY();
        shape.setTranslateX(x + delta.getX());
        shape.setTranslateY(y + delta.getY());
        // wrap if needed
        wrap();
    }

    /** Updates the position and direction of this object */
    public abstract void update();

}
