package cs1302.omega;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HighScoreScreen {

    private static Path HIGHSCORES_FILE = Path.of("resources/highscores");
    private static Path DEFAULT_HIGHSCORES_FILE = Path.of("resources/highscores_default");
    private Scene scene;
    private List<Record> highScores;
    private TableView<Record> table;

    public HighScoreScreen(int width, int height) {
        highScores = new ArrayList<>();
        try {
            loadFromFile(HIGHSCORES_FILE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ObservableList<Record> data = FXCollections.observableList(highScores);

        TableColumn<Record, Integer> scoreCol = new TableColumn<>("Score");
        TableColumn<Record, String> nameCol = new TableColumn<>("Name");
        TableColumn<Record, LocalDate> dateCol = new TableColumn<>("Date");

        scoreCol.setCellValueFactory(
                new PropertyValueFactory<Record, Integer>("score"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Record, String>("name"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Record, LocalDate>("date"));
        table = new TableView<>(data);
        table.getColumns().add(scoreCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(dateCol);
        

        Text reset = new Text("RESET");
        reset.setFill(Color.WHITE);
        reset.setFont(OmegaApp.F40);
        reset.setOnMouseEntered(event -> reset.setFill(Color.RED));
        reset.setOnMouseExited(event -> reset.setFill(Color.WHITE));

        Text back = new Text("BACK");
        back.setFill(Color.WHITE);
        back.setFont(OmegaApp.F40);
        back.setOnMouseEntered(event -> back.setFill(Color.RED));
        back.setOnMouseExited(event -> back.setFill(Color.WHITE));

        Region space1 = new Region();
        HBox.setHgrow(space1, Priority.ALWAYS);
        Region space2 = new Region();
        HBox.setHgrow(space2, Priority.ALWAYS);
        Region space3 = new Region();
        HBox.setHgrow(space3, Priority.ALWAYS);

        HBox menu = new HBox();
        menu.setAlignment(Pos.CENTER);
        menu.setPrefHeight(60);
        menu.getChildren().addAll(space1, reset, space2, back, space3);

        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        scene = new Scene(root, width, height);
        root.getChildren().addAll(table, menu);
    }

    public boolean canAddRecord(int score) {
        for (Record record : highScores) {
            if (score >= record.getScore()) {
                return true;
            }
        }
        return false;
    }

    public void addRecord(int score) {
        for (int i = 0; i < highScores.size(); i++) {
            if (score >= highScores.get(i).getScore()) {
                Record record = new Record(score, "enter name", LocalDate.now());
                highScores.add(i, record);
                break;
            }
        }
        highScores.remove(10);
        table.setItems(FXCollections.observableList(highScores));
    }

    /**
     * 
     * @param filePath
     * @throws IOException
     */
    private void loadFromFile(Path filePath) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(filePath)) {
            while (r.ready()) {
                String line = r.readLine();
                String[] parts = line.split(",");
                int score = Integer.parseInt(parts[0]);
                String name = parts[1];
                LocalDate date = LocalDate.parse(parts[2]);
                highScores.add(new Record(score, name, date));
            }
        }
    }

    private void saveToFile(Path filePath) throws IOException {
        OpenOption createNew = StandardOpenOption.CREATE_NEW;
        try (BufferedWriter w = Files.newBufferedWriter(filePath, createNew)) {
            for (int i = 0; i < highScores.size(); i++) {
                StringBuilder entry = new StringBuilder();
                entry.append(highScores.get(i).getScore());
                entry.append(',');
                entry.append(highScores.get(i).getName());
                entry.append(',');
                entry.append(highScores.get(i).getDate());
                w.append(entry);
            }
        }
    }

    private void reset() {

    }

    public Scene getScene() {
        return scene;
    }
}
