package cs1302.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Projectile extends AnimatedObject {

    private int timeLeft;

    public Projectile(Game game, int displayTime) {
        super(game);
        shape = new Rectangle(4, 4);
        shape.setFill(Color.AQUAMARINE);
        timeLeft = displayTime;
    }
    
    public int getTimeLeft() {
        return timeLeft;
    }

    @Override
    public void update() {
        updateDirection();
        updatePosition();
        timeLeft = Math.max(0, timeLeft - 1);
    }

}
