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
	
	/**
	 * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
	 * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
	 */
	public OmegaApp() {
	}

	/** {@inheritDoc} */
	@Override
	public void start(Stage stage) {

		VBox root = new VBox();
		root.setAlignment(Pos.CENTER);
		Font f1 = Font.font("C059", FontWeight.MEDIUM, 40);

        Text newgame = new Text("NEW GAME");
        newgame.setFill(Color.WHITE);
        newgame.setFont(f1);
        newgame.setOnMouseEntered(event -> newgame.setFill(Color.RED));
        newgame.setOnMouseExited(event -> newgame.setFill(Color.WHITE));
        
        Text highscore = new Text("HIGH SCORES");
        highscore.setFill(Color.WHITE);
        highscore.setFont(f1);
        highscore.setOnMouseEntered(event -> highscore.setFill(Color.RED));
        highscore.setOnMouseExited(event -> highscore.setFill(Color.WHITE)); 
        
        Text settings = new Text("SETTINGS");
        settings.setFill(Color.WHITE);
        settings.setFont(f1);
        settings.setOnMouseEntered(event -> settings.setFill(Color.RED));
        settings.setOnMouseExited(event -> settings.setFill(Color.WHITE)); 
        
        Text help = new Text("HELP");
        help.setFill(Color.WHITE);
        help.setFont(f1);
        help.setOnMouseEntered(event -> help.setFill(Color.RED));
        help.setOnMouseExited(event -> help.setFill(Color.WHITE)); 
        
        Text exit = new Text("EXIT");
        exit.setFill(Color.WHITE);
        exit.setFont(f1);
        exit.setOnMouseEntered(event -> exit.setFill(Color.RED));
        exit.setOnMouseExited(event -> exit.setFill(Color.WHITE));
        exit.setOnMouseClicked(event -> { 
        	Node source = (Node)event.getSource();
        	Stage window = (Stage)source.getScene().getWindow();
        	window.close();
        });
        
        root.setSpacing(10);
        // an empty space to push the visible elements up a little
        Region empty = new Region();
        empty.setPrefHeight(100);
        root.getChildren().addAll(newgame, highscore, settings, help, exit, empty);
		
        Scene scene = new Scene(root, 680, 640);
		scene.setFill(Color.BLACK);

		// setup stage
		stage.setTitle("Asteroids!");
		stage.setScene(scene);
		stage.setOnCloseRequest(event -> Platform.exit());
		stage.sizeToScene();
		stage.show();
		Platform.runLater(() -> stage.setResizable(false));
		System.out.println(stage.getHeight());
		System.out.println(stage.getWidth());

        System.out.println(root.getHeight());
		System.out.println(root.getWidth());

	} // start

} // OmegaApp
