package cs1302.omega;

import java.time.LocalDate;

/**
 * Class representing a high score record. Each high score has a score, a name
 * and a date component.
 * 
 */
public class Record {
    /** The score. */
    private int score;
    /** The name. */
    private String name;
    /** The date. */
    private LocalDate date;

    /**
     * Creates a new Record with the specified {@code score}, {@code name} and
     * {@code date}.
     * 
     * @param score the score
     * @param name  the name
     * @param date  the date
     */
    public Record(int score, String name, LocalDate date) {
        this.score = score;
        this.date = date;
        this.name = name;
    }

    /**
     * Returns the score component of this record.
     * 
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the name component of this record.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the date component of this record.
     * 
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the score component of this record.
     * 
     * @param score the score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Sets the name component of this record.
     * 
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the date component of this record.
     * 
     * @param date the date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }
}
