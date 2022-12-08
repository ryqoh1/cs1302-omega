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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
import javafx.util.converter.DefaultStringConverter;

/**
 * Class representing the High Scores screen.
 * 
 */
public class HighScoreScreen {

    /** File that stores the current records */
    private static Path HIGHSCORES_FILE = Path.of("resources/highscores");
    /** File that stores the factory default records */
    private static Path DEFAULT_HIGHSCORES_FILE = Path.of("resources/highscores_default");
    private static Background BLACK = new Background(
            new BackgroundFill(Color.BLACK, null, null));

    /** The main scene of this screen */
    private Scene scene;
    /** The high scores data */
    private List<Record> highScores;
    /** The high scores table */
    private TableView<Record> table;
    /** The editable(newly added) row's index */
    private int editIndex;

    /**
     * 
     * Creates a new HighScoreScreen with the specified {@code width} and
     * {@code height}.
     * 
     * @param width  the width of this screen
     * @param height the height of this screen
     * @param app    the app containing this screen
     */
    public HighScoreScreen(int width, int height, OmegaApp app) {
        editIndex = -1;
        highScores = new ArrayList<>();
        try {
            loadFromFile(HIGHSCORES_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // initialize table
        initTable();
        // menu item to reset high score to factory default
        Text reset = new Text("RESET");
        reset.setFill(Color.WHITE);
        reset.setFont(OmegaApp.F40);
        reset.setOnMouseEntered(event -> reset.setFill(Color.RED));
        reset.setOnMouseExited(event -> reset.setFill(Color.WHITE));
        reset.setOnMouseClicked(event -> reset());
        // menu item to leave this screen to the main menu
        Text done = new Text("DONE");
        done.setFill(Color.WHITE);
        done.setFont(OmegaApp.F40);
        done.setOnMouseEntered(event -> done.setFill(Color.RED));
        done.setOnMouseExited(event -> done.setFill(Color.WHITE));
        done.setOnMouseClicked(event -> {
            try {
                // save before leaving
                saveToFile(HIGHSCORES_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            app.displayMainMenu();
        });
        // empty spaces for better alignment
        Region space1 = new Region();
        HBox.setHgrow(space1, Priority.ALWAYS);
        Region space2 = new Region();
        HBox.setHgrow(space2, Priority.ALWAYS);
        Region space3 = new Region();
        HBox.setHgrow(space3, Priority.ALWAYS);
        // menu at the bottom
        HBox menu = new HBox();
        menu.setAlignment(Pos.CENTER);
        menu.setPrefHeight(60);
        menu.getChildren().addAll(space1, reset, space2, done, space3);

        VBox root = new VBox();
        root.setBackground(BLACK);
        scene = new Scene(root, width, height);
        root.getChildren().addAll(table, menu);
    }

    /**
     * Creates and initializes the high scores table.
     * 
     */
    private void initTable() {
        // initialize columns
        TableColumn<Record, Integer> scoreCol = initScoreColumn();
        TableColumn<Record, String> nameCol = initNameColumn();
        TableColumn<Record, LocalDate> dateCol = initDateColumn();
        // set data source for the table
        ObservableList<Record> data = FXCollections.observableList(highScores);
        table = new TableView<>(data);
        table.getColumns().add(scoreCol);
        table.getColumns().add(nameCol);
        table.getColumns().add(dateCol);
        // make the table stretch vertically
        VBox.setVgrow(table, Priority.ALWAYS);
        // a cell is only editable if the column and table it elongs to are also
        // editable
        table.setEditable(true);
        nameCol.setEditable(true);
        // hide column headers
        // https://stackoverflow.com/questions/27118872/how-to-hide-tableview-column-header-in-javafx-8
        table.skinProperty().addListener((a, b, newSkin) -> {
            Pane header = (Pane) table.lookup("TableHeaderRow");
            header.setMinHeight(0);
            header.setPrefHeight(0);
            header.setMaxHeight(0);
            header.setVisible(false);
        });
        // give columns a fixed width based on the table's width
        scoreCol.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
        dateCol.prefWidthProperty().bind(table.widthProperty().multiply(0.296));
        scoreCol.setResizable(false);
        nameCol.setResizable(false);
        dateCol.setResizable(false);
    }

    /**
     * Creates and initializes the score column.
     * 
     * @return the initialized column
     */
    private TableColumn<Record, Integer> initScoreColumn() {
        TableColumn<Record, Integer> scoreCol = new TableColumn<>();
        // set the "score" field of the Record class as source
        scoreCol.setCellValueFactory(new PropertyValueFactory<Record, Integer>("score"));
        // set custom cells for this column
        scoreCol.setCellFactory(col -> {
            TableCell<Record, Integer> cell = new TextFieldTableCell<>();
            // set cell style
            cell.setBackground(BLACK);
            cell.setTextFill(Color.WHITE);
            cell.setAlignment(Pos.CENTER);
            cell.setFont(OmegaApp.F24);
            return cell;
        });

        return scoreCol;
    }

    /**
     * Creates and initializes the name column.
     * 
     * @return the initialized column
     */
    private TableColumn<Record, String> initNameColumn() {
        TableColumn<Record, String> nameCol = new TableColumn<>();
        // set the "name" field of the Record class as source
        nameCol.setCellValueFactory(new PropertyValueFactory<Record, String>("name"));
        // set custom editable cells for this column
        nameCol.setCellFactory(col -> {
            // cell has a text field for editing
            TextFieldTableCell<Record, String> cell = new TextFieldTableCell<>() {
                @Override
                public void startEdit() {
                    // only one row(the newly added one) may be editable
                    if (getIndex() != editIndex) {
                        return;
                    }
                    super.startEdit();
                }
            };
            // set the cells editable
            cell.setEditable(true);
            cell.setConverter(new DefaultStringConverter());
            // set style
            cell.setBackground(BLACK);
            cell.setTextFill(Color.WHITE);
            cell.setAlignment(Pos.CENTER);
            cell.setFont(OmegaApp.F24);

            return cell;
        });
        // update record after editing
        nameCol.setOnEditCommit(event -> {
            ((Record) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow())).setName(event.getNewValue());
        });

        return nameCol;
    }

    /**
     * Creates and initializes the date column.
     * 
     * @return the initialized column
     */
    private TableColumn<Record, LocalDate> initDateColumn() {
        TableColumn<Record, LocalDate> dateCol = new TableColumn<>();
        // set the "date" field of the Record class as source
        dateCol.setCellValueFactory(new PropertyValueFactory<Record, LocalDate>("date"));
        // set custom cells for this column
        dateCol.setCellFactory(col -> {
            TableCell<Record, LocalDate> cell = new TextFieldTableCell<>();
            cell.setBackground(BLACK);
            cell.setTextFill(Color.WHITE);
            cell.setAlignment(Pos.CENTER);
            cell.setFont(OmegaApp.F24);
            return cell;
        });
        return dateCol;
    }

    /**
     * Returns whether there is a record in the table with less than or equal score
     * as the specified score.
     * 
     * @param score the score
     * @return {@code true} if a record with this score can be inserted to the
     *         table, {@code false} otherwise
     */
    public boolean canAddRecord(int score) {
        for (Record record : highScores) {
            if (score >= record.getScore()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new row to the table with the specified score, the current date and a
     * default name. The new row is inserted at a place that keeps the rows in
     * descending order by score. The name column of the new row will be editable
     * after this method executes.
     * 
     * @param score the score
     */
    public void addRecord(int score) {
        for (int i = 0; i < highScores.size(); i++) {
            if (score >= highScores.get(i).getScore()) {
                Record record = new Record(score, "<ENTER NAME>", LocalDate.now());
                highScores.add(i, record);
                editIndex = i;
                break;
            }
        }
        // remove the extra row
        highScores.remove(10);
        // reload table data
        table.setItems(FXCollections.observableList(highScores));
    }

    /**
     * Loads records from the file at the specified path.
     * 
     * @param filePath path to the file
     * @throws IOException if I/O error happens during execution
     */
    private void loadFromFile(Path filePath) throws IOException {
        highScores.clear();
        // try with resources
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

    /**
     * Saves records to the file at the specified path.
     * 
     * @param filePath path to the file
     * @throws IOException if I/O error happens during execution
     */
    private void saveToFile(Path filePath) throws IOException {
        // old content of the file should be deleted
        OpenOption truncate = StandardOpenOption.TRUNCATE_EXISTING;
        // try with resources
        try (BufferedWriter w = Files.newBufferedWriter(filePath, truncate)) {
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

    /**
     * Resets records to factory defaults.
     */
    private void reset() {
        try {
            loadFromFile(DEFAULT_HIGHSCORES_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        table.setItems(FXCollections.observableList(highScores));
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
