package ru.kpfu.itis.selyantsev.app;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.kpfu.itis.selyantsev.controller.StartScreen;

public class FlappyBird extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        new StartScreen(stage);
    }

}
