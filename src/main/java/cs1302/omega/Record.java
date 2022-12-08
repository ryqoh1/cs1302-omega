package cs1302.omega;

import java.time.LocalDate;

public class Record {
    private int score;
    private String name;
    private LocalDate date;
    
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

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
