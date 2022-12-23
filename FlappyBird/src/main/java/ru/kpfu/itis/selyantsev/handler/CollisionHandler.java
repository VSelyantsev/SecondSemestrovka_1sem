package ru.kpfu.itis.selyantsev.handler;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class CollisionHandler {
    public boolean collisionDetection(ArrayList<Rectangle> obstacles, Rectangle bird) {
        boolean flag = false;
        for (Rectangle rectangle : obstacles) {
            if (rectangle.getBoundsInParent().intersects(bird.getBoundsInParent())) {
                flag = true;
            }
        }
        return flag;
    }

}
