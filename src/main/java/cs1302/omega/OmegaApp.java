package cs1302.omega;

import cs1302.game.DemoGame;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.Node;
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

	public static final int SCENE_WIDTH = 680;
	public static final int SCENE_HEIGHT = 640;

	public static final Font F40 = Font.font("C059", FontWeight.MEDIUM, 40);
	public static final Font F24 = Font.font("C059", FontWeight.MEDIUM, 24);
	
	Scene mainMenu;
	Scene gameScreen;
	
	Stage stage;

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
		initGameScreen();

		// setup stage
		stage.setTitle("Asteroids!");
		stage.setScene(mainMenu);
		stage.setOnCloseRequest(event -> Platform.exit());
		stage.sizeToScene();
		stage.show();
		Platform.runLater(() -> stage.setResizable(false));
	} // start

	/**
	 * Initializes the main menu screen.
	 */
	private void initMainMenu() {
		// Menu item that start a new game
		Text newGame = getMainMenuItem("NEW GAME");
		newGame.setOnMouseClicked(event -> {
			System.out.println(gameScreen.getHeight());
			stage.setScene(gameScreen);
		});
		// Menu item that displays high scores
		Text highScore = getMainMenuItem("HIGH SCORES");
		highScore.setOnMouseClicked(event -> System.out.println("unimplemented"));
		// Menu item that displays settings
		Text settings = getMainMenuItem("SETTINGS");
		settings.setOnMouseClicked(event -> System.out.println("unimplemented"));
		// Menu item that displays help
		Text help = getMainMenuItem("HELP");
		help.setOnMouseClicked(event -> System.out.println("unimplemented"));
		// Menu item that exits the application
		Text exit = getMainMenuItem("EXIT");
		exit.setOnMouseClicked(event -> {
			Node source = (Node) event.getSource();
			Stage window = (Stage) source.getScene().getWindow();
			window.close();
		});
		// an empty space to push the visible elements up a little
		Region empty = new Region();
		empty.setPrefHeight(100);

		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		root.setSpacing(10);
		root.getChildren().addAll(newGame, highScore, settings, help, exit, empty);

		mainMenu = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		mainMenu.setFill(Color.BLACK);
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

	private void initGameScreen() {
		// demo game provided with the starter code
		DemoGame game = new DemoGame(640, 240);

		VBox root = new VBox();

		HBox infoArea = new HBox();

		infoArea.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		infoArea.setPrefHeight(60);

		HBox scoreArea = new HBox();
		scoreArea.maxHeight(Double.MAX_VALUE);
		scoreArea.setPrefWidth(200);
		scoreArea.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		scoreArea.setAlignment(Pos.CENTER_RIGHT);

		Text score = new Text("999999");
		score.setFill(Color.WHITE);
		score.setFont(F40);
		score.setTextAlignment(TextAlignment.CENTER);
		score.maxHeight(Double.MAX_VALUE);
		scoreArea.getChildren().add(score);

		HBox livesArea = new HBox();
		livesArea.maxHeight(Double.MAX_VALUE);
		livesArea.setPrefWidth(200);
		livesArea.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		livesArea.setAlignment(Pos.CENTER);

		Text lives = new Text("O".repeat(5));
		lives.setFill(Color.WHITE);
		lives.setFont(F40);
		lives.setTextAlignment(TextAlignment.CENTER);
		lives.maxHeight(Double.MAX_VALUE);
		livesArea.getChildren().add(lives);

		Text info = new Text("PRESS ANY KEY");
		info.setFill(Color.WHITE);
		info.setFont(F24);
		info.setTextAlignment(TextAlignment.CENTER);
		info.maxHeight(Double.MAX_VALUE);

		Region space1 = new Region();
		HBox.setHgrow(space1, Priority.ALWAYS);

		Region space2 = new Region();
		HBox.setHgrow(space2, Priority.ALWAYS);

		infoArea.setAlignment(Pos.CENTER);
		infoArea.getChildren().add(livesArea);
		infoArea.getChildren().add(space1);
		infoArea.getChildren().add(info);
		infoArea.getChildren().add(space2);
		infoArea.getChildren().add(scoreArea);

		root.getChildren().add(infoArea);
		root.getChildren().add(game);

		gameScreen = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		gameScreen.setFill(Color.BLACK);

	}

} // OmegaApp
