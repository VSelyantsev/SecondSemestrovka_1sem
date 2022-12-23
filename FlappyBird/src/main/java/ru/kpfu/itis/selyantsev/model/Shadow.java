package ru.kpfu.itis.selyantsev.model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.UUID;

public class Shadow extends AnchorPane {
    private UUID id;
    private Rectangle shadow;
    private int jumpHeight;

    public Shadow(UUID id, Rectangle shadow, int jumpHeight) {
        this.id = id;
        this.shadow = shadow;
        this.jumpHeight = jumpHeight;
    }
    public void fly() {
        double movement = -jumpHeight;
        if (shadow.getLayoutY() + shadow.getY() <= jumpHeight) {
            movement = -(shadow.getLayoutY() + shadow.getY());
        }
        moveBirdY(movement);
    }
    public void moveBirdY(double positionChange) {
        shadow.setY(shadow.getY() + positionChange);
    }
    public UUID getUUID() {
        return id;
    }
}
