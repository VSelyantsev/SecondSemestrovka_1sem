package ru.kpfu.itis.selyantsev.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StartScreen {
    private AnchorPane parentRoot;
    private Text gameNameTextField;
    private final Controller mainController = Controller.getInstance();
    private VBox vBox;
    private Stage startScreenStage;
    public Button startGameButton;
    public Button multiplayerBottom;
    public StartScreen(Stage stage) throws FileNotFoundException {
        this.startScreenStage = stage;
        init();
    }

    private Image setStartScreenImageView(String pathToImage) throws FileNotFoundException {
        FileInputStream inputStreamForImage = new FileInputStream(pathToImage);
        return new Image(inputStreamForImage);
    }

    public void init() throws FileNotFoundException {
        parentRoot = new AnchorPane();
        vBox = new VBox(15);
        String startScreenImagePath = "C:\\SecondSemest_1sem\\FlappyBird\\src\\main\\resources\\images.background\\bg_5.png";
        ImageView startScreenImageView = new ImageView(setStartScreenImageView(startScreenImagePath));
        startScreenImageView.setFitHeight(600);
        startScreenImageView.setFitWidth(400);

        gameNameTextField = new Text("Flappy Bird!");
        gameNameTextField.setFill(Color.BLACK);
        Font font = Font.font("Courier New", FontWeight.BOLD, 30);
        gameNameTextField.setFont(font);

        startGameButton = new Button("Start");
        startGameButton.setOnAction(soloGameEvent);
        startGameButton.setMaxHeight(400);
        startGameButton.setMaxWidth(250);

        multiplayerBottom = new Button("Multiplayer Gaming");
        multiplayerBottom.setOnAction(multiplayerGameEvent);
        multiplayerBottom.setMaxHeight(400);
        multiplayerBottom.setMaxWidth(250);

        vBox.getChildren().add(gameNameTextField);
        vBox.getChildren().add(startGameButton);
        vBox.getChildren().add(multiplayerBottom);

        AnchorPane.setTopAnchor(vBox, 300.0);
        AnchorPane.setLeftAnchor(vBox, 60.0);
        AnchorPane.setRightAnchor(vBox, 10.0);
        AnchorPane.setBottomAnchor(vBox, 100.0);
        parentRoot.getChildren().add(startScreenImageView);
        parentRoot.getChildren().add(vBox);

        Scene scene = new Scene(parentRoot, 400, 600);
        startScreenStage.setScene(scene);
        startScreenStage.setTitle("Flappy Bird");
        startScreenStage.show();
    }

    private final EventHandler<ActionEvent> soloGameEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (startGameButton == event.getSource()) {
                mainController.setStage(startScreenStage);
                mainController.soloGame();
            }
        }
    };

    private final EventHandler<ActionEvent> multiplayerGameEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if (multiplayerBottom == event.getSource()) {
                mainController.setStage(startScreenStage);
                mainController.multiplayerGame();
            }
        }
    };
}
