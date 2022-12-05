package cs1302.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import cs1302.game.Asteroid.AsteroidType;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

/**
 * An example of a simple game in JavaFX. The play can move the rectangle
 * left/right with the arrow keys or teleport the rectangle by clicking it!
 */
public class DemoGame extends Game {

    private Ship player;
    private List<Asteroid> asteroids = new ArrayList<>();
    private Random rnd = new Random();
    int c = 60;
    /**
     * Construct a {@code DemoGame} object.
     * 
     * @param width  scene width
     * @param height scene height
     */
    public DemoGame(int width, int height) {
        super(width, height, 60); // call parent constructor
        setLogLevel(Level.INFO); // enable logging
        this.player = new Ship(this);
    } // DemoGame

    /** {@inheritDoc} */
    @Override
    protected void init() {
        // setup subgraph for this component
        getChildren().addAll(player.getShape());
        player.move(new Point2D(0, 0));
        for (int i = 0; i < 1; i++) {
            Asteroid a = new Asteroid(this, AsteroidType.MEDIUM);
            asteroids.add(a);
//            a.move(new Point2D(rnd.nextDouble(500 - 50), rnd.nextDouble(500 - 50)));
            a.setVelocity(new Point2D(1, 0));
            a.setSpin((rnd.nextDouble(1) - 0.5));
            System.out.println(a.getVelocity());
            getChildren().add(a.getShape());
        }
    } // init

    /** {@inheritDoc} */
    @Override
    protected void update() {

        // update player position
        isKeyPressed(KeyCode.LEFT, () -> player.move(new Point2D(-10, 0)));
        isKeyPressed(KeyCode.RIGHT, () -> player.move(new Point2D(10, 0)));

        // update player position
        isKeyPressed(KeyCode.UP, () -> player.move(new Point2D(0, -10)));
        isKeyPressed(KeyCode.DOWN, () -> player.move(new Point2D(0, 10)));

        // don't rotate twice from keyboard and mouse
        boolean rotationHandled = false;
        if (isKeyPressed(KeyCode.D, () -> player.rotate(4))) {
            rotationHandled = true;
        } else if (isKeyPressed(KeyCode.A, () -> player.rotate(-4))) {
            rotationHandled = true;
        }

//        if (!rotationHandled && isMouseButtonPressed()) {
//            rotateShipToCursor(getLastMousePressedEvent());
//        }

        if (!isKeyPressed(KeyCode.W)) {
            player.setEnginesOn(false);
        } else {
            player.setEnginesOn(true);
        }
        

        //player.update();
        for (Asteroid a : asteroids) {
            c --;
            if (c == 0) {
                a.update();
                c = 5;
            }

        }
    } // update

//    /**
//     * Rotates the ship towards the mouse cursor.
//     * 
//     * @param event associated mouse event
//     */
//    private void rotateShipToCursor(MouseEvent event) {
//
//        // logger.info(event.toString());
//        Bounds playerBounds = player.getShape().getBoundsInParent();
//        Point2D click = new Point2D(event.getX(), event.getY());
//
//        double angle = getAngle(
//                new Point2D(playerBounds.getCenterX(), playerBounds.getCenterY()), click);
//        double oldDir = player.getDirectionInDegrees();
//
//        if (angle < 0 && oldDir < 0) {
//            if (angle < oldDir) { // left
//                player.rotate(Math.max(angle, oldDir - 5));
//            } else { // right
//                player.rotate(Math.min(angle, oldDir + 5));
//            }
//        } else if (angle < 0 && oldDir >= 0) {
//            if (angle + 180 > oldDir) { // left
//                double tmpDir = oldDir - 5;
//                if (tmpDir < angle) {
//                    player.rotate(angle);
//                }
//            } else { // right
//                double tmpDir = oldDir + 5;
//                if (tmpDir > 180) {
//                    player.rotate(tmpDir - 360);
//                }
//            }
//        } else if (angle >= 0 && oldDir >= 0) {
//            if (angle < oldDir) { // left
//                player.rotate(Math.max(angle, oldDir - 5));
//            } else { // right
//                player.rotate(Math.min(angle, oldDir + 5));
//            }
//        } else if (angle >= 0 && oldDir < 0) {
//            if (angle - 180 > oldDir) { // left
//                double tmpDir = oldDir - 5;
//                if (tmpDir < -180) {
//                    player.rotate(tmpDir + 360);
//                }
//            } else { // right
//                double tmpDir = oldDir - 5;
//                if (tmpDir > angle) {
//                    player.rotate(angle);
//                }
//            }
//        }
//
//        double newDir = player.getDirectionInDegrees();
//        if (Math.abs(oldDir - newDir) > 5) {
//            System.out.println(angle + " " + oldDir + " " + newDir);
//        }
//        System.out.println(angle + " " + oldDir + " " + newDir);
//
//    }

    
    
    private double getAngle(Point2D p1, Point2D p2) {
        double theta = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
        double angle = Math.toDegrees(theta);
        return angle;
    }

} // DemoGame
