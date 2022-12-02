package cs1302.omega;

import java.util.ArrayList;
import java.util.List;

import cs1302.game.DemoGame;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class GameScreen {
	
	private Scene scene;
	private DemoGame game;
	private Text score;
	private Text info;
	private List<ImageView> lives;
	
	public GameScreen(int width, int height) {
		initGameScreen(width, height);
	}

	private void initGameScreen(int width, int height) {		
		HBox scoreArea = new HBox();
		scoreArea.setPrefWidth(200);
		scoreArea.setAlignment(Pos.CENTER_RIGHT);
		score = new Text();
		score.setFill(Color.WHITE);
		score.setFont(OmegaApp.F40);
		score.setTextAlignment(TextAlignment.CENTER);
		scoreArea.getChildren().add(score);

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
		
		info = new Text("PRESS ANY KEY TO START");
		info.setFill(Color.WHITE);
		info.setFont(OmegaApp.F18);
		info.setTextAlignment(TextAlignment.CENTER);

		Region space1 = new Region();
		HBox.setHgrow(space1, Priority.ALWAYS);
		Region space2 = new Region();
		HBox.setHgrow(space2, Priority.ALWAYS);

		HBox infoArea = new HBox();
		infoArea.setPrefHeight(60);
		infoArea.setAlignment(Pos.CENTER);
		infoArea.getChildren().add(livesArea);
		infoArea.getChildren().add(space1);
		infoArea.getChildren().add(info);
		infoArea.getChildren().add(space2);
		infoArea.getChildren().add(scoreArea);

		game = new DemoGame(width, height - 60);
		game.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));

		VBox root = new VBox();
		root.getChildren().add(infoArea);
		root.getChildren().add(game);
		
		scene = new Scene(root, width, height);
		scene.setFill(Color.BLACK);
		displayScore(2000);
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public void play() {
		game.play();
	}
	
	public void displayInfo(String text) {
		info.setText(text);
	}
	
	public void displayScore(int score) {
		this.score.setText(String.format("%07d", score));
	}
	
	public void displayLives(int lives) {
		for (int i = 0; i < lives; i++) {
			this.lives.get(i).setVisible(true);
		}
		
		for (int i = lives; i < 5; i++) {
			this.lives.get(i).setVisible(false);
		}
	}
}
