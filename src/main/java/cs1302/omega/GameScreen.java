package cs1302.omega;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs1302.game.DemoGame;
import cs1302.game.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Class representing the game screen.
 *
 */
public class GameScreen {

    /** The allowed maximum number of lives */
    public static final int MAX_LIVES = 5;
    /** The allowed maximum score */
    public static final int MAX_SCORE = 9999999;
    /** The height of the info area in pixels */
    public static final int INFO_HEIGHT = 60;

    private Scene scene;
    private Game game;
    private Text score;
    private Text info;
    private List<ImageView> lives;

    /**
     * Creates a new GameScreen with the specified {@code width} and {@code height}.
     * The height of the actual game area will be {@code height} -
     * {@link #INFO_HEIGHT}.
     * 
     * @param width
     * @param height
     */
    public GameScreen(int width, int height) {
        initGameScreen(width, height);
    }

    private void initGameScreen(int width, int height) {

        Pane scoreArea = initScoreArea();
        displayScore(0);
        Pane livesArea = initLivesArea();

        info = new Text("PRESS ANY KEY \n TO START");
        info.setFill(Color.WHITE);
        info.setFont(OmegaApp.F18);
        info.setTextAlignment(TextAlignment.CENTER);
        // empty spaces used to align the info text to the center
        Region space1 = new Region();
        HBox.setHgrow(space1, Priority.ALWAYS);
        Region space2 = new Region();
        HBox.setHgrow(space2, Priority.ALWAYS);

        HBox infoArea = new HBox();
        infoArea.setPrefHeight(INFO_HEIGHT);
        infoArea.setMinHeight(INFO_HEIGHT);
        infoArea.setAlignment(Pos.CENTER);
        // the layout must have a non-transparent background, otherwise game objects may
        // appear on top of it
        infoArea.setBackground(
                new Background(new BackgroundFill(Color.BLACK, null, null)));
        // border to visually separate the game area and the info area
        infoArea.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                        new BorderWidths(0, 0, 2, 0, false, false, false, false))));

        infoArea.getChildren().add(livesArea);
        infoArea.getChildren().add(space1);
        infoArea.getChildren().add(info);
        infoArea.getChildren().add(space2);
        infoArea.getChildren().add(scoreArea);

        game = new DemoGame(width, height - INFO_HEIGHT, this);
        game.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        // this makes the game area render after the info area without affecting the
        // layout
        game.setViewOrder(1);

        VBox root = new VBox();
        root.getChildren().add(infoArea);
        root.getChildren().add(game);

        scene = new Scene(root, width, height);
        scene.setFill(Color.BLACK);
    }

    /**
     * Initializes the screen area used to display the game score.
     * 
     * @return the layout of this area
     */
    private Pane initScoreArea() {
        HBox scoreArea = new HBox();
        scoreArea.setPrefWidth(200);
        scoreArea.setAlignment(Pos.CENTER_RIGHT);

        score = new Text();
        score.setFill(Color.WHITE);
        score.setFont(OmegaApp.F40);
        score.setTextAlignment(TextAlignment.CENTER);

        scoreArea.getChildren().add(score);
        return scoreArea;
    }

    /**
     * Initializes the screen area used to display the number of lives.
     * 
     * @return the layout of this area
     */
    private Pane initLivesArea() {
        HBox livesArea = new HBox();
        livesArea.maxHeight(Double.MAX_VALUE);
        livesArea.setPrefWidth(200);
        livesArea.setAlignment(Pos.CENTER);
        livesArea.setSpacing(10);

        Image ship = new Image("file:resources/game/ship.png");
        lives = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ImageView life = new ImageView(ship);
            lives.add(life);
            livesArea.getChildren().add(life);
        }

        return livesArea;
    }

    /**
     * Returns the main scene of this screen.
     * 
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Activates the game.
     */
    public void play() {
        game.play();
    }

    /**
     * Displays the specified text in the info area.
     * 
     * @param text the text to display
     * @throws NullPointerException if {@code text} is null
     */
    public void displayInfo(String text) {
        Objects.requireNonNull(text);
        info.setText(text);
    }

    /**
     * Displays the specified score in the info area.
     * 
     * @param score the score to display
     * @throws IllegalArgumentException if {@code score} is negative, or greater
     *                                  than {@link #MAX_SCORE}
     */
    public void displayScore(int score) {
        if (score < 0 || score > MAX_SCORE) {
            throw new IllegalStateException("invalid score: " + score);
        }
        this.score.setText(String.format("%07d", score));
    }

    /**
     * Displays the specified number of lives in the info area.
     * 
     * @param lives number of lives
     * @throws IllegalArgumentException if {@code lives} is negative, or greater
     *                                  than {@link #MAX_LIVES}
     */
    public void displayLives(int lives) {
        if (lives < 0 || lives > MAX_LIVES) {
            throw new IllegalStateException("invalid number of lives: " + lives);
        }
        // set all life indicators visible, then hide the unneeded ones.
        for (int i = 0; i < lives; i++) {
            this.lives.get(i).setVisible(true);
        }
        for (int i = lives; i < MAX_LIVES; i++) {
            this.lives.get(i).setVisible(false);
        }
    }
}
