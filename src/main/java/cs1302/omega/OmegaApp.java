package cs1302.omega;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class OmegaApp extends Application {
	
	static final int SCENE_WIDTH = 680;
	static final int SCENE_HEIGHT = 640;
	
	static final Font F40 = Font.font("C059", FontWeight.MEDIUM, 40);
	
	Scene mainMenu;
	
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

        initMainMenu();
        
        
		// setup stage
		stage.setTitle("Asteroids!");
		stage.setScene(mainMenu);
		stage.setOnCloseRequest(event -> Platform.exit());
		stage.sizeToScene();
		stage.show();
		Platform.runLater(() -> stage.setResizable(false));
	} // start
	
	private void initMainMenu() {
		
		Text newGame = new Text("NEW GAME");
	    newGame.setFill(Color.WHITE);
	    newGame.setFont(F40);
	    newGame.setOnMouseEntered(event -> newGame.setFill(Color.RED));
	    newGame.setOnMouseExited(event -> newGame.setFill(Color.WHITE));
	    
	    Text highScore = new Text("HIGH SCORES");
	    highScore.setFill(Color.WHITE);
	    highScore.setFont(F40);
	    highScore.setOnMouseEntered(event -> highScore.setFill(Color.RED));
	    highScore.setOnMouseExited(event -> highScore.setFill(Color.WHITE)); 
	    
	    Text settings = new Text("SETTINGS");
	    settings.setFill(Color.WHITE);
	    settings.setFont(F40);
	    settings.setOnMouseEntered(event -> settings.setFill(Color.RED));
	    settings.setOnMouseExited(event -> settings.setFill(Color.WHITE)); 
	    
	    Text help = new Text("HELP");
	    help.setFill(Color.WHITE);
	    help.setFont(F40);
	    help.setOnMouseEntered(event -> help.setFill(Color.RED));
	    help.setOnMouseExited(event -> help.setFill(Color.WHITE)); 
	    
	    Text exit = new Text("EXIT");
	    exit.setFill(Color.WHITE);
	    exit.setFont(F40);
	    exit.setOnMouseEntered(event -> exit.setFill(Color.RED));
	    exit.setOnMouseExited(event -> exit.setFill(Color.WHITE));
	    exit.setOnMouseClicked(event -> { 
	    	Node source = (Node)event.getSource();
	    	Stage window = (Stage)source.getScene().getWindow();
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

} // OmegaApp
