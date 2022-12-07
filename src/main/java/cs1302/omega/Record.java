package cs1302.omega;

import java.time.LocalDate;

public class Record {
    private final int score;
    private final String name;
    private final LocalDate date;
    
    public Record(int score, String name, LocalDate date) {
        this.score = score;
        this.date = date;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }
}
