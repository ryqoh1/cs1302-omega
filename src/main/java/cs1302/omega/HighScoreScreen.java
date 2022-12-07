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

import javafx.scene.Scene;

public class HighScoreScreen {

    private static Path HIGHSCORES_FILE = Path.of("resources/highscores");
    private static Path DEFAULT_HIGHSCORES_FILE = Path.of("resources/highscores_default");
    private Scene scene;
    private List<Record> highScores;

    public HighScoreScreen(int width, int height) {
        highScores = new ArrayList<>();
        try {
            loadFromFile(HIGHSCORES_FILE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean canAddRecord(int score) {
        for (Record record : highScores) {
            if (score >= record.getScore()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean addRecord(int score) {
        for (Record record : highScores) {
            if (score >= record.getScore()) {
                return true;
            }
        }
        return false;
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
