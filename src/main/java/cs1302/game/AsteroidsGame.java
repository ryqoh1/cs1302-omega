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

/**
 * Implementation of the Asteroids game.
 * @see The Help menu for more information on gameplay
 */
public class AsteroidsGame extends Game {

    /** Player's ship. */
    private Ship player;
    /** Asteroids. */
    private List<Asteroid> asteroids = new ArrayList<>();
    /** Projectiles. */
    private List<Projectile> projectiles = new ArrayList<>();
    /** Random number generator. */
    private Random rnd = new Random();
    /** Game score. */
    private int score = 0;
    /** Lives. */
    private int lives = 3;
    /** Whether the game is paused, waiting for user interaction. */
    private boolean waitingForInteraction;
    /** Time left from invulnerability. */
    private int shipInvulnerable;
    /** GameScreen containing this game. */
    private GameScreen gameScreen;

    /**
     * Constructs a {@code AsteroidsGame} object with the specified {@code width},
     * {@code height} and {@code GameScreen}.
     * 
     * @param width width of the game area
     * @param height height of the game area
     * @param gameScreen the GameScreen containing this game
     */
    public AsteroidsGame(int width, int height, GameScreen gameScreen) {
        super(width, height, 60); // call parent constructor
        this.gameScreen = gameScreen;
        setLogLevel(Level.INFO); // enable logging
    } 

    /** {@inheritDoc} */
    @Override
    protected void init() {
        this.player = new Ship(this);
        // ship should be rendered last
        this.player.getShape().setViewOrder(-1);
        player.setMaxSpeed(5);
        player.setWeaponCooldown(30);
        getChildren().addAll(player.getShape());
        // move to the center, facing up
        player.move(new Point2D(getWidth() / 2 - 15, getHeight() / 2 - 15));
        player.rotate(-90.0);
        // spawn asteroids
        spawnInitialAsteroids();
        // call updates to put everything in place
        player.update();
        updateAsteroids();
        gameScreen.displayLives(lives);
        // pause the game
        waitingForInteraction = true;
    } // init

    /** {@inheritDoc} */
    @Override
    protected void update() {
        // handle unpause after the player died
        if (waitingForInteraction) {
            if (lives == 0) {
                // no more lives left, end the game
                if (isKeyPressed(KeyCode.ENTER)) {
                    stop();
                    gameScreen.afterGame(score);
                }
            } else {
                // continue
                if (isKeyPressed(KeyCode.ENTER)) {
                    waitingForInteraction = false;
                    gameScreen.displayInfo("");
                }
            }
            return;
        }
        // end round if there are no asteroids left
        if (asteroids.isEmpty()) {
            handleRoundEnd();
            return;
        }
        // handle controls
        handleShipControls();
        // update objects
        player.update();
        updateAsteroids();
        updateProjectiles();
        shipInvulnerable = Math.max(0, shipInvulnerable - 1);
        // flash the ship and the lives during invulnerability
        if (shipInvulnerable > 0) {
            if ((shipInvulnerable / 20) % 2 == 0) {
                player.getShape().setVisible(true);
                gameScreen.displayLives(lives);
            } else {
                player.getShape().setVisible(false);
                gameScreen.displayLives(0);
            }
        }
        // update game screen
        gameScreen.displayScore(score);
    } // update

    /**
     * Updates the asteroids.
     */
    private void updateAsteroids() {
        List<Asteroid> asteroidsToRemove = new ArrayList<>();
        List<Asteroid> asteroidsToAdd = new ArrayList<>();

        for (Asteroid asteroid : asteroids) {
            asteroid.update();
            // collision with player
            if (asteroid.collidesWith(player)) {
                handlePlayerCollision();
            }
            // collision with projectile
            for (Projectile p : projectiles) {
                if (asteroid.collidesWith(p)) {
                    // split asteroid
                    List<Asteroid> newAsteroids = asteroid.split();
                    // get the central coordinates of the asteroid
                    Bounds objectBounds = asteroid.getShape().getBoundsInParent();
                    double cx = objectBounds.getCenterX();
                    double cy = objectBounds.getCenterY();
                    for (Asteroid newAsteroid : newAsteroids) {
                        // move it to the position of the destroyed asteroid
                        newAsteroid.move(new Point2D(cx, cy));
                        // move it a bit to a random direction
                        newAsteroid.move(new Point2D(rnd.nextDouble(10) - 5,
                                rnd.nextDouble(10) - 5));
                        newAsteroid.randomizeMovement();
                    }
                    // projectile will disappear in the next update
                    p.setTimeLeft(0);
                    // remove destroyed asteroid
                    asteroidsToRemove.add(asteroid);
                    // add new asteroids
                    asteroidsToAdd.addAll(newAsteroids);
                    // asteroid is already destroyed, break out from the loop
                    break;
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

    /**
     * Updates the projectiles.
     */
    private void updateProjectiles() {
        List<Projectile> projectilesToRemove = new ArrayList<>();
        for (Projectile p : projectiles) {
            // remove projectiles with no display time left
            if (p.getTimeLeft() == 0) {
                projectilesToRemove.add(p);
                getChildren().remove(p.getShape());
            }
            p.update();
        }

        projectiles.removeAll(projectilesToRemove);
    }

    /**
     * Handles ship control events.
     */
    private void handleShipControls() {
        // fire weapon if the ship is not invulnerable
        if (shipInvulnerable == 0 && isKeyPressed(KeyCode.SPACE)) {
            if (player.fire()) {
                spawnProjectile();
            }
        }
        // don't rotate twice from keyboard and mouse
        boolean rotationHandled = false;
        // rotate from keys
        if (isKeyPressed(KeyCode.D, () -> player.rotate(4.0))) {
            rotationHandled = true;
        } else if (isKeyPressed(KeyCode.A, () -> player.rotate(-4.0))) {
            rotationHandled = true;
        }
        // rotate from moouse event
        if (!rotationHandled && isMouseButtonPressed()) {
            rotateShipToCursor(getLastMousePressedEvent());
        }
        // apply thrust
        if (!isKeyPressed(KeyCode.W)) {
            player.setEnginesOn(false);
        } else {
            player.setEnginesOn(true);
        }
    }

    /**
     * Handles player collision.
     */
    private void handlePlayerCollision() {
        if (shipInvulnerable == 0) {
            lives--;
            if (lives > 0) {
                // 5s invulnerability after death
                shipInvulnerable = 300;
                // pause and wait for player interaction
                waitingForInteraction = true;
                gameScreen.displayInfo("PRESS ENTER\nTO CONTINUE");
            } else {
                // pause and wait for player interaction
                waitingForInteraction = true;
                gameScreen.displayInfo("PRESS ENTER\nTO EXIT GAME");
            }
        }
    }

    /**
     * Increases the score based on the specified destroyed asteroid.
     * 
     * @param asteroid the destroyed asteroid
     */
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
     * Handle the end of a round.
     */
    private void handleRoundEnd() {
        // add 20000 score for finishing the round
        score += 20000;
        // add one life or 20000 score
        if (lives == 5) {
            score += 20000;
        } else {
            lives++;
        }
        // remove everything from the game area
        projectiles.clear();
        getChildren().clear();
        // add initial asteroids
        spawnInitialAsteroids();
        update();
        // re-add player
        getChildren().add(player.getShape());
        // new round starts with 5s invulnerability
        shipInvulnerable = 300;
        // pause
        gameScreen.displayLives(lives);
        gameScreen.displayInfo("PRESS ENTER\nTO CONTINUE");
        waitingForInteraction = true;
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
        Projectile p = new Projectile(AsteroidsGame.this, 120);
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

    /**
     * Rotates the ship towards the mouse cursor.
     * 
     * @param event associated mouse event
     */
    private void rotateShipToCursor(MouseEvent event) {
        Point2D click = new Point2D(event.getX(), event.getY());
        player.rotateToPoint(click, 4.0);
    }
}
