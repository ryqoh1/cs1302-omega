package cs1302.game;

import java.util.Arrays;
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

    public static enum AsteroidType {
        SMALL, MEDIUM, LARGE
    };

    private static final Image ASTEROID = new Image(
            "file:resources/game/Generic_Celestia_asteroid_texture.jpg");
    private static final PixelReader READER = ASTEROID.getPixelReader();
    private static final int IMG_WIDTH = (int) ASTEROID.getWidth();
    private static final int IMG_HEIGHT = (int) ASTEROID.getHeight();

    public Asteroid(Game game) {
        this(game, AsteroidType.LARGE);
    }

    public Asteroid(Game game, AsteroidType type) {
        super(game);
        int size = 10;
        switch (type) {
        case SMALL:
            size = 20;
            break;
        case MEDIUM:
            size = 40;
            break;
        case LARGE:
            size = 60;
        }

        shape = createPolygon(size);

        // asteroid objects are using a randomly generated subimage same image as the
        // source of background texture
        Random rnd = new Random();
        int x = rnd.nextInt(IMG_WIDTH - size - 30);
        int y = rnd.nextInt(IMG_HEIGHT - size - 30);
        WritableImage image = new WritableImage(READER, x, y, size, size);
        shape.setFill(new ImagePattern(image));
    }

    @Override
    public void update() {
        updateDirection();
        updatePosition();
    }

    private static Polygon createPolygon(double size) {

        Polygon polygon = new Polygon();
        double c1 = Math.cos(Math.PI * 2 / 5);
        double c2 = Math.cos(Math.PI / 5);
        double s1 = Math.sin(Math.PI * 2 / 5);
        double s2 = Math.sin(Math.PI * 4 / 5);

        polygon.getPoints().addAll(size, 0.0, size * c1, -1 * size * s1, -1 * size * c2,
                -1 * size * s2, -1 * size * c2, size * s2, size * c1, size * s1);

        return polygon;
    }
}
