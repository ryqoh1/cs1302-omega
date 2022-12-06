package cs1302.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

/**
 * An animated object representing an asteroid.
 */
public class Asteroid extends AnimatedObject {

    /**
     * Asteroid type.
     */
    public static enum AsteroidType {
        SMALL, MEDIUM, LARGE
    };

    private static final Image IMAGE = new Image(
            "file:resources/game/Generic_Celestia_asteroid_texture.jpg");
    private static final PixelReader READER = IMAGE.getPixelReader();

    /** The type of this asteroid */
    private final AsteroidType type;

    /**
     * Creates a new Asteroid with the specified Game.
     * 
     * @param game he game containing this asteroid
     * @throws NullPointerException if the game is null
     */
    public Asteroid(Game game) {
        this(game, AsteroidType.LARGE);
    }

    /**
     * Creates a new Asteroid with the specified Game and AsteroidType.
     * 
     * @param game the game containing this asteroid
     * @param type the type of this asteroid
     * @throws NullPointerException if the game is null
     * @throws NullPointerException if the type is null
     */
    public Asteroid(Game game, AsteroidType type) {
        super(game);
        this.type = type;

        int size = 0;
        switch (type) {
        case SMALL:
            size = 15;
            break;
        case MEDIUM:
            size = 30;
            break;
        case LARGE:
            size = 50;
        }

        shape = getRandomPolygon(size, 15);

        // asteroid objects are using a randomly generated subimage of same image as the
        // source of background texture
        Random rnd = new Random();
        int safety = 30;
        int x = rnd.nextInt((int) IMAGE.getWidth() - size - safety);
        int y = rnd.nextInt((int) IMAGE.getHeight() - size - safety);
        WritableImage image = new WritableImage(READER, x, y, size, size);
        shape.setFill(new ImagePattern(image));
    }

    /**
     * Returns the type of this asteroid.
     * 
     * @return the type
     */
    public AsteroidType getType() {
        return type;
    }

    /**
     * Randomizes the constant movement(velocity and spin) of this asteroid.
     */
    public void randomizeMovement() {
        Random rnd = new Random();
        Point2D baseVelocity = new Point2D(rnd.nextDouble(2) - 1, rnd.nextDouble(2) - 1);
        double baseSpin = rnd.nextDouble(2.0) - 1.0;
        switch (type) {
        case SMALL:
            spin = baseSpin * 3;
            velocity = baseVelocity.multiply(3.0);
            break;
        case MEDIUM:
            spin = baseSpin;
            velocity = baseVelocity;
            break;
        case LARGE:
            spin = baseSpin * 0.4;
            velocity = baseVelocity.multiply(0.4);
        }
    }

    /**
     * Splits this asteroid into smaller ones.
     * 
     * @return a list containing the newly created asteroids
     */
    public List<Asteroid> split() {
        List<Asteroid> result = new ArrayList<>();

        switch (type) {
        case SMALL:
            // small asteroids can't be split further
            break;
        case MEDIUM:
            // split into 3 small ones
            for (int i = 0; i < 3; i++) {
                result.add(new Asteroid(game, AsteroidType.SMALL));
            }
            break;
        case LARGE:
            // split into 3 medium and 1 small ones
            for (int i = 0; i < 3; i++) {
                result.add(new Asteroid(game, AsteroidType.MEDIUM));
            }
            for (int i = 0; i < 1; i++) {
                result.add(new Asteroid(game, AsteroidType.SMALL));
            }
            break;
        default:
            throw new IllegalStateException("unhandled enum type");
        }

        return result;

    }

    @Override
    public void update() {
        updateDirection();
        updatePosition();
    }

    /**
     * Creates and returns a new, randomly generated polygon with the specified size
     * and number of verticles.
     * 
     * @param size          radius of a circle containing this asteroid
     * @param verticleCount the number of verticles of the the polygon
     * @return the new Polygon
     */
    private Polygon getRandomPolygon(int size, int verticleCount) {

        double[] verticles = new double[verticleCount * 2];

        Random rnd = new Random();
        double angleDelta = 360.0 / verticleCount;
        // angle of the current verticle
        double angle = 0;
        // vector length of the previous verticle
        double prevLength = 3 * (size / 4.0);
        // allowed difference between two neighoring verticles vectors
        double allowedDiff = size / 8.0;

        for (int index = 0; index < verticleCount; index++) {
            // length of the vector pointing to the next verticle
            double length = rnd.nextInt(size / 2) + (size / 2.0);
            // in order to reduce "spikes", large differences are not allowed
            if (length + allowedDiff < prevLength) {
                length = prevLength - allowedDiff;
            } else if (length - allowedDiff > prevLength) {
                length = prevLength + allowedDiff;
            }
            // construct the next verticle
            angle += angleDelta;
            verticles[index * 2] = length * Math.cos(Math.toRadians(angle));
            verticles[index * 2 + 1] = length * Math.sin(Math.toRadians(angle));
            ;

            prevLength = length;
        }

        return new Polygon(verticles);
    }
}
