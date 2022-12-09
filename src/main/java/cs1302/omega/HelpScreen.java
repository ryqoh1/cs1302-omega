package cs1302.omega;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Class representing the help screen.
 *  
 */
public class HelpScreen {
    /** File that stores the help content */
    private static Path HELP_FILE = Path.of("resources/help");

    /** The main scene of this screen */
    private Scene scene;

    /**
     * 
     * Creates a new HelpScreen with the specified {@code width} and {@code height}.
     * 
     * @param width  the width of this screen
     * @param height the height of this screen
     * @param app    the app containing this screen
     */
    public HelpScreen(int width, int height, OmegaApp app) {
        // display help content
        TextArea help = new TextArea();
        help.setWrapText(true);
        help.setEditable(false);
        help.setFont(OmegaApp.F18);
        help.setStyle("-fx-control-inner-background: black");
        VBox.setVgrow(help, Priority.ALWAYS);
        // load content from file with try with resources
        try (BufferedReader reader = Files.newBufferedReader(HELP_FILE)) {
            StringBuilder content = new StringBuilder();
            while (reader.ready()) {
                content.append(reader.readLine() + "\n");
            }
            help.setText(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // back to main menu
        Text back = new Text("BACK TO MAIN MENU");
        back.setFill(Color.WHITE);
        back.setFont(OmegaApp.F40);
        back.setOnMouseEntered(event -> back.setFill(Color.RED));
        back.setOnMouseExited(event -> back.setFill(Color.WHITE));
        back.setOnMouseClicked(event -> app.displayMainMenu());

        HBox menu = new HBox();
        menu.setAlignment(Pos.CENTER);
        menu.setPrefHeight(60);
        menu.getChildren().add(back);

        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        scene = new Scene(root, width, height);
        root.getChildren().addAll(help, menu);
    }

    /**
     * Returns the main scene of this screen.
     * 
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }
}
