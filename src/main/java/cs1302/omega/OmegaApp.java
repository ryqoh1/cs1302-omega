package cs1302.omega;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Implementation of the classic arcade game Asteroids!.
 * <p>
 * 
 * @see <a href=
 *      "https://en.wikipedia.org/wiki/Asteroids_(video_game)">Wikipedia</a>
 */
public class OmegaApp extends Application {

    public static final int SCENE_WIDTH = 620;
    public static final int SCENE_HEIGHT = 680;

    public static final Font F40 = Font.font("C059", FontWeight.MEDIUM, 40);
    public static final Font F24 = Font.font("C059", FontWeight.MEDIUM, 24);
    public static final Font F18 = Font.font("C059", FontWeight.NORMAL, 18);
    public static final Font F14 = Font.font("C059", FontWeight.NORMAL, 14);

    Scene mainMenu;
    Stage stage;
    HighScoreScreen highScoreScreen;
    HelpScreen helpScreen;

    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {
    }

    /** {@inheritDoc} */
    @Override
    public void init() {

    }

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        initMainMenu();
        highScoreScreen = new HighScoreScreen(SCENE_WIDTH, SCENE_HEIGHT, this);
        helpScreen = new HelpScreen(SCENE_WIDTH, SCENE_HEIGHT, this);
        
        // setup stage
        stage.setTitle("Asteroids!");
        stage.setScene(mainMenu);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
        Platform.runLater(() -> stage.setResizable(false));
    } // start

    public void afterGame(int score) {
        if (highScoreScreen.canAddRecord(score)) {
            highScoreScreen.addRecord(score);
            stage.setScene(highScoreScreen.getScene());
        } else {
            stage.setScene(mainMenu);
        }
    }
    
    public void displayMainMenu() {
        stage.setScene(mainMenu);
    }

    /**
     * Initializes the main menu screen.
     */
    private void initMainMenu() {
        // new game
        Text newGame = getMainMenuItem("NEW GAME");
        newGame.setOnMouseClicked(event -> {
            GameScreen gs = new GameScreen(SCENE_WIDTH, SCENE_HEIGHT, this);
            stage.setScene(gs.getScene());
            gs.show();
        });
        // high scores
        Text highScore = getMainMenuItem("HIGH SCORES");
        highScore.setOnMouseClicked(event -> stage.setScene(highScoreScreen.getScene()));
        // settings
        Text settings = new Text("SETTINGS");
        settings.setFill(Color.GREY);
        settings.setFont(F40);
        settings.setOnMouseClicked(event -> System.out.println("unimplemented"));
        // help
        Text help = getMainMenuItem("HELP");
        help.setOnMouseClicked(event -> stage.setScene(helpScreen.getScene()));
        // exit
        Text exit = getMainMenuItem("EXIT");
        exit.setOnMouseClicked(event -> {
            stage.close();
        });
        // an empty space to push the visible elements up a little
        Region empty = new Region();
        empty.setPrefHeight(100);

        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.getChildren().addAll(newGame, highScore, settings, help, exit, empty);

        mainMenu = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    }

    /**
     * Creates and returns a basic main menu item.
     * 
     * @param text - the text to be displayed
     * @return the menu item
     */
    private Text getMainMenuItem(String text) {
        Text item = new Text(text);
        item.setFill(Color.WHITE);
        item.setFont(F40);
        item.setOnMouseEntered(event -> item.setFill(Color.RED));
        item.setOnMouseExited(event -> item.setFill(Color.WHITE));
        return item;
    }
} // OmegaApp
