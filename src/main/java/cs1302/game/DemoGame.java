package cs1302.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import cs1302.game.Asteroid.AsteroidType;
import cs1302.omega.GameScreen;
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
    private List<Projectile> projectiles = new ArrayList<>();
    private Random rnd = new Random();
    private int score = 0;
    private int lives = 3;
    private boolean waitingForInteraction;
    private int shipInvulnerable;

    private GameScreen gameScreen;

    /**
     * Construct a {@code DemoGame} object.
     * 
     * @param width  scene width
     * @param height scene height
     */
    public DemoGame(int width, int height, GameScreen gameScreen) {
        super(width, height, 60); // call parent constructor
        this.gameScreen = gameScreen;
        setLogLevel(Level.INFO); // enable logging
        this.player = new Ship(this);
        // ship should be rendered last
        this.player.getShape().setViewOrder(-1);
        player.setMaxSpeed(5);
        player.setWeaponCooldown(30);
    } // DemoGame

    /** {@inheritDoc} */
    @Override
    protected void init() {
        // setup subgraph for this component
        getChildren().addAll(player.getShape());
        player.move(new Point2D(getWidth() / 2 - 15, getHeight() / 2 - 15));
        player.rotate(-90.0);
        gameScreen.displayLives(lives);
        spawnInitialAsteroids();
        update();
        waitingForInteraction = true;
    } // init

    /** {@inheritDoc} */
    @Override
    protected void update() {

        if (waitingForInteraction) {
            if (isKeyPressed(KeyCode.ENTER)) {
                waitingForInteraction = false;
                gameScreen.displayInfo("");
            }
            return;
        }

        if (shipInvulnerable == 0 && isKeyPressed(KeyCode.SPACE)) {
            if (player.fire()) {
                // spawn projectile
                spawnProjectile();
            }
        }

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

        player.update();
        shipInvulnerable = Math.max(0, shipInvulnerable - 1);

        if (shipInvulnerable > 0) {
            if ((shipInvulnerable / 20) % 2 == 0) {
                player.getShape().setVisible(true);
            } else {
                player.getShape().setVisible(false);
            }
        }
        
        updateAsteroids();

        updateProjectiles();
        // update game screen
        gameScreen.displayScore(score);
    } // update

    private void updateAsteroids() {
        List<Asteroid> asteroidsToRemove = new ArrayList<>();
        List<Asteroid> asteroidsToAdd = new ArrayList<>();

        for (Asteroid asteroid : asteroids) {
            asteroid.update();

            if (asteroid.collidesWith(player)) {
                handlePlayerCollision();
            }

            for (Projectile p : projectiles) {
                if (asteroid.collidesWith(p)) {
                    p.setTimeLeft(0);
                    asteroidsToRemove.add(asteroid);
                    List<Asteroid> newAsteroids = asteroid.split();
                    Bounds objectBounds = asteroid.getShape().getBoundsInParent();
                    double cx = objectBounds.getCenterX();
                    double cy = objectBounds.getCenterY();
                    for (Asteroid aa : newAsteroids) {
                        aa.move(new Point2D(cx, cy));
                        aa.move(new Point2D(rnd.nextDouble(10) - 5,
                                rnd.nextDouble(10) - 5));
                        aa.randomizeMovement();
                    }

                    asteroidsToAdd.addAll(newAsteroids);
                }
            }
        }

        // handle destroyed asteroids
        asteroids.removeAll(asteroidsToRemove);
        for (Asteroid asteroid : asteroidsToRemove) {
            getChildren().remove(asteroid.getShape());
            increaseScore(asteroid);
        }

        // manage the new asteroids
        asteroids.addAll(asteroidsToAdd);
        for (Asteroid a : asteroidsToAdd) {
            getChildren().add(a.getShape());
        }
    }

    private void updateProjectiles() {
        List<Projectile> projectilesToRemove = new ArrayList<>();
        for (Projectile p : projectiles) {
            if (p.getTimeLeft() == 0) {
                projectilesToRemove.add(p);
                getChildren().remove(p.getShape());
            }
            p.update();
        }

        projectiles.removeAll(projectilesToRemove);
    }

    private void handlePlayerCollision() {
        if (shipInvulnerable == 0) {
            waitingForInteraction = true;
            lives--;
            gameScreen.displayLives(lives);
            gameScreen.displayInfo("PRESS ENTER\nTO CONTINUE");
            shipInvulnerable = 300;
        }
    }

    private void increaseScore(Asteroid asteroid) {
        // overall score should be 30000 per round
        switch (asteroid.getType()) {
        case LARGE:
            score += 145;
            break;
        case MEDIUM:
            score += 285;
            break;
        case SMALL:
            score += 500;
            break;
        default:
            throw new IllegalStateException("unhandled enum type");
        }
    }

    /**
     * Spawns a new projectile from the player's ship.
     */
    private void spawnProjectile() {
        // get the central coordinates of the ship
        Bounds shipBounds = player.getShape().getBoundsInParent();
        double scx = shipBounds.getCenterX();
        double scy = shipBounds.getCenterY();
        // create a projectile with ~2s display time
        Projectile p = new Projectile(DemoGame.this, 120);
        // get the central coordinates of the projectile
        Bounds pBounds = p.getShape().getBoundsInParent();
        double pcx = pBounds.getCenterX();
        double pcy = pBounds.getCenterY();
        // move the projectile to the center of the ship
        p.move(new Point2D(scx - pcx, scy - pcy));
        // move the projectile closer to the front of the ship
        p.move(player.getDirection().multiply(10));
        // velocity vector should point to the direction the ship is facing
        p.setVelocity(player.getDirection().multiply(7));
        // also rotate the projectile itself
        p.rotate(player.getDirectionInDegrees());
        p.update();
        // manage the projectile
        getChildren().add(p.getShape());
        projectiles.add(p);
    }

    /**
     * Spawns the initial asteroids at the start of a round.
     */
    private void spawnInitialAsteroids() {
        for (int i = 0; i < 5; i++) {
            // create a new large asteroid
            Asteroid asteroid = new Asteroid(this, AsteroidType.LARGE);
            // get central coordinates of the game area
            double centerX = getWidth() / 2;
            double centerY = getHeight() / 2;
            double x = centerX;
            double y = centerY;
            // try random spawn points until they are not in the central 200x200 box
            while (Math.abs(x - centerX) < 100 || Math.abs(y - centerY) < 100) {
                x = rnd.nextDouble(getWidth());
                y = rnd.nextDouble(getHeight());
            }
            asteroid.move(new Point2D(x, y));
            asteroid.randomizeMovement();
            // manage the asteroid
            getChildren().add(asteroid.getShape());
            asteroids.add(asteroid);
        }
    }

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
