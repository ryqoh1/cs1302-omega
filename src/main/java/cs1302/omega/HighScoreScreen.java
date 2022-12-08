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
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class HighScoreScreen {

    private static Path HIGHSCORES_FILE = Path.of("resources/highscores");
    private static Path DEFAULT_HIGHSCORES_FILE = Path.of("resources/highscores_default");
    private static Background BLACK = new Background(
            new BackgroundFill(Color.BLACK, null, null));

    private Scene scene;
    private List<Record> highScores;
    private TableView<Record> table;
    private int editIndex = -1;
    private TableColumn<Record, String> nameCol;
    private OmegaApp app;

    public HighScoreScreen(int width, int height, OmegaApp app) {
        this.app = app;
        highScores = new ArrayList<>();
        try {
            loadFromFile(HIGHSCORES_FILE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ObservableList<Record> data = FXCollections.observableList(highScores);

        TableColumn<Record, Integer> scoreCol = new TableColumn<>("Score");
        nameCol = new TableColumn<>("Name");
        TableColumn<Record, LocalDate> dateCol = new TableColumn<>("Date");

        scoreCol.setCellValueFactory(new PropertyValueFactory<Record, Integer>("score"));
        scoreCol.setCellFactory(col -> {
            TableCell<Record, Integer> cell = new TextFieldTableCell<>();
            cell.setBackground(BLACK);
            cell.setTextFill(Color.WHITE);
            cell.setAlignment(Pos.CENTER);
            cell.setFont(OmegaApp.F24);
            return cell;
        });
        nameCol.setCellValueFactory(new PropertyValueFactory<Record, String>("name"));
        nameCol.setCellFactory(col -> {
            TextFieldTableCell<Record, String> cell = new TextFieldTableCell<>() {
                @Override
                public void startEdit() {
                    if (getIndex() != editIndex) {
                        return;
                    }
                    super.startEdit();
                }
            };

            cell.setEditable(true);
            cell.setConverter(new DefaultStringConverter());
            cell.setBackground(BLACK);
            cell.setTextFill(Color.WHITE);
            cell.setAlignment(Pos.CENTER);
            cell.setFont(OmegaApp.F24);
            
            return cell;
        });
        // https://stackoverflow.com/questions/18721394/can-javafx-tableview-be-partly-editable
        nameCol.setOnEditCommit(event -> {
            ((Record) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow())).setName(event.getNewValue());
        });
        dateCol.setCellValueFactory(new PropertyValueFactory<Record, LocalDate>("date"));
        dateCol.setCellFactory(col -> {
            TableCell<Record, LocalDate> cell = new TextFieldTableCell<>();
            cell.setBackground(BLACK);
            cell.setTextFill(Color.WHITE);
            cell.setAlignment(Pos.CENTER);
            cell.setFont(OmegaApp.F24);
            return cell;
        });
        table = new TableView<>(data);
        table.getColumns().add(scoreCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(dateCol);
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setEditable(true);
        nameCol.setEditable(true);
        table.setStyle("-fx-table-cell-border-color: transparent;");

//        https://stackoverflow.com/questions/27118872/how-to-hide-tableview-column-header-in-javafx-8
        table.skinProperty().addListener((a, b, newSkin) -> {
            Pane header = (Pane) table.lookup("TableHeaderRow");
            header.setMinHeight(0);
            header.setPrefHeight(0);
            header.setMaxHeight(0);
            header.setVisible(false);
        });

        scoreCol.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
        dateCol.prefWidthProperty().bind(table.widthProperty().multiply(0.296));

        scoreCol.setResizable(false);
        nameCol.setResizable(false);
        dateCol.setResizable(false);

        Text reset = new Text("RESET");
        reset.setFill(Color.WHITE);
        reset.setFont(OmegaApp.F40);
        reset.setOnMouseEntered(event -> reset.setFill(Color.RED));
        reset.setOnMouseExited(event -> reset.setFill(Color.WHITE));
        reset.setOnMouseClicked(event -> reset());
        
        Text done = new Text("DONE");
        done.setFill(Color.WHITE);
        done.setFont(OmegaApp.F40);
        done.setOnMouseEntered(event -> done.setFill(Color.RED));
        done.setOnMouseExited(event -> done.setFill(Color.WHITE));
        done.setOnMouseClicked(event -> {
            try {
                saveToFile(HIGHSCORES_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            app.displayMainMenu();
        });

        
        Region space1 = new Region();
        HBox.setHgrow(space1, Priority.ALWAYS);
        Region space2 = new Region();
        HBox.setHgrow(space2, Priority.ALWAYS);
        Region space3 = new Region();
        HBox.setHgrow(space3, Priority.ALWAYS);

        HBox menu = new HBox();
        menu.setAlignment(Pos.CENTER);
        menu.setPrefHeight(60);
        menu.getChildren().addAll(space1, reset, space2, done, space3);

        VBox root = new VBox();
        root.setBackground(BLACK);
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
                Record record = new Record(score, "<ENTER NAME>", LocalDate.now());
                highScores.add(i, record);
                editIndex = i;
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
        highScores.clear();
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
        OpenOption createNew = StandardOpenOption.TRUNCATE_EXISTING;
        try (BufferedWriter w = Files.newBufferedWriter(filePath, createNew)) {
            for (int i = 0; i < highScores.size(); i++) {
                StringBuilder entry = new StringBuilder();
                entry.append(highScores.get(i).getScore());
                entry.append(',');
                entry.append(highScores.get(i).getName());
                entry.append(',');
                entry.append(highScores.get(i).getDate());
                entry.append(System.lineSeparator());
                w.append(entry);
            }
        }
    }

    private void reset() {
        try {
            loadFromFile(DEFAULT_HIGHSCORES_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        table.setItems(FXCollections.observableList(highScores));
    }

    public Scene getScene() {
        return scene;
    }
}
