package cs1302.game;

import java.util.Random;
import java.util.logging.Level;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
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
    private Point2D velocity = new Point2D(0,0);


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

        // don't rotate twice from keyboard and mouse
        boolean rotationHandled = false;
        if (isKeyPressed( KeyCode.D, () -> rotateRight())) {
            rotationHandled = true;
        } else if (isKeyPressed( KeyCode.A, () -> rotateLeft())) {
            rotationHandled = true;
        }
           
        Point2D movChange = new Point2D(0,0);
        if (!rotationHandled && isMouseButtonPressed()) {
            rotateShipToCursor(getLastMousePressedEvent());
        }
        
        if (!isKeyPressed( KeyCode.W, () -> player.setFill(new ImagePattern(shipon)))) {
            player.setFill(new ImagePattern(shipoff));
        } else {
            double dir = player.getRotate();
            double changeX = Math.cos(Math.toRadians(dir));
            double changeY = Math.sin(Math.toRadians(dir));

            movChange = new Point2D(changeX / 10, changeY / 10);
        }
        
        
        
        Point2D tmpShipMovement = velocity.add(movChange);
        double movX = tmpShipMovement.getX();
        double movY = tmpShipMovement.getY();
        double speed = tmpShipMovement.magnitude();
        double mult = 1;
        if (Math.abs(speed) > 10) {
            mult = 10 / Math.abs(speed);
        }
        
        velocity = new Point2D(movX * mult, movY * mult);

        
        player.setX(player.getX() + velocity.getX());
        player.setY(player.getY() + velocity.getY());
        
        wrap();
    } // update

    private void wrap() {
        Bounds gameBounds = getGameBounds();
        Bounds playerBounds = player.getBoundsInParent();
        
        double x = playerBounds.getCenterX();
        double y = playerBounds.getCenterY();
        
        int m = 10;
        
        if (x - m > gameBounds.getMaxX()) {
            player.setX(-m);
        } else if (x + m < gameBounds.getMinX()) {
            player.setX(gameBounds.getMaxX() - m);
        }
        
        if (y - m > gameBounds.getMaxY()) {
            player.setY(-m);
        } else if (y + m < gameBounds.getMinY()) {
            player.setY(gameBounds.getMaxY() - m);
        }
        
    }
    
    
    
    /**
     * Rotates the ship towards the mouse cursor.
     * @param event associated mouse event
     */
    private void rotateShipToCursor(MouseEvent event) {
        
        //logger.info(event.toString());
        Bounds playerBounds = player.getBoundsInParent();
        Point2D click = new Point2D(event.getX(),event.getY());
        double angle = getAngle(new Point2D(playerBounds.getCenterX(), playerBounds.getCenterY()), click);
        System.out.println(angle + " " + player.getRotate());
        double oldR = player.getRotate();
        if (angle < 0) {
          player.setRotate(Math.max(angle, player.getRotate() - 5));
        } else if (angle > 0) {
          player.setRotate(Math.min(angle, player.getRotate() + 5));
        }
    }
    
    private double getAngle(Point2D p1, Point2D p2) {
        double theta = Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX());
        double angle = Math.toDegrees(theta);
        return angle;
    }
    
    private void rotateLeft() {
        double newRotate = player.getRotate() - 4;
        if (newRotate < -180) {
            newRotate = 360 + newRotate;
        }
        player.setRotate(newRotate);
    }
    
    private void rotateRight() {
        double newRotate = player.getRotate() + 4;
        if (newRotate > 180) {
            newRotate = -(360 - newRotate);
        }
        player.setRotate(newRotate);
    }

} // DemoGame
