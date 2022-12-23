package ru.kpfu.itis.selyantsev.model;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import ru.kpfu.itis.selyantsev.handler.CollisionHandler;

import java.util.ArrayList;
import java.util.UUID;

public class Bird {
    private Rectangle bird;
    private int jumpHeight;
    private UUID randomId;
    CollisionHandler collisionHandler = new CollisionHandler();

    public Bird(UUID randomId, Rectangle bird, int jumpHeight) {
        this.randomId = randomId;
        this.bird = bird;
        this.jumpHeight = jumpHeight;
    }

    public void fly() {
        double movement = -jumpHeight;
        if (bird.getLayoutY() + bird.getY() <= jumpHeight) {
            movement = -(bird.getLayoutY() + bird.getY());
        }
        moveBirdY(movement);
    }

    public void moveBirdY(double positionChange) {
        bird.setY(bird.getY() + positionChange);
    }

    public boolean isBirdDead(ArrayList<Rectangle> obstacles, AnchorPane plane) {
        double birdY = bird.getLayoutY() + bird.getY();
        boolean temp = collisionHandler.collisionDetection(obstacles, bird);
        if (temp) {
            return true;
        } else {
            return birdY >= plane.getHeight();
        }
    }

    public UUID getRandomId() {
        return randomId;
    }

    public Rectangle getBirdRectangle() {
        return bird;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }
}
