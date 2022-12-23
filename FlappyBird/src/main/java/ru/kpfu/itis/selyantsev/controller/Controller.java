package ru.kpfu.itis.selyantsev.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.kpfu.itis.selyantsev.handler.ObstaclesHandler;
import ru.kpfu.itis.selyantsev.model.Bird;
import ru.kpfu.itis.selyantsev.model.Shadow;
import ru.kpfu.itis.selyantsev.server.SocketClient;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Controller {
    AnimationTimer gameLoop;
    private CopyOnWriteArrayList<Shadow> shadowList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Bird> birdList = new CopyOnWriteArrayList<>();
    private ScheduledExecutorService executorService;
    private AnchorPane plane = new AnchorPane();
    private Rectangle bird;
    private Text score;
    private double accelerationTime = 0;
    private int gameTime = 0;
    private int scoreCounter = 0;
    private SocketClient socketClient;
    private Bird birdComponent;
    private ObstaclesHandler obstaclesHandler;
    ArrayList<Rectangle> obstacles = new ArrayList<>();
    private static final Controller mainController = new Controller();
    private static Stage primaryStage;
    private static Scene scene;
    public Controller() {
    }
    public void createClientAndSendMessage(String message) {
        socketClient = new SocketClient("localhost", 7777);
        socketClient.setController(this);
        socketClient.start();
        socketClient.sendMessage(message);
    }
    public void createMultipleClientsAndSendMessage(String message) {
        socketClient = new SocketClient("localhost", 7777);
        socketClient.setController(this);
        socketClient.start();
        socketClient.sendMessage(message);
        executorService = new ScheduledThreadPoolExecutor(10);
    }
    public void configureSoloGame() {
        // init AnchorPane
        score = new Text();

        // creating birComponent
        bird = new Rectangle(50.0, 50.0);
        bird.setX(50);
        bird.setY(50);
        bird.setFill(Color.ORANGE);
        int jumpHeight = 75;
        UUID randomId = UUID.randomUUID();
        birdComponent = new Bird(randomId, bird, jumpHeight);
        birdList.add(birdComponent);

        // init attributes for handler
        double planeHeight = 600;
        double planeWidth = 400;
        //init handler for Obstacles (Rectangles)
        obstaclesHandler = new ObstaclesHandler(plane, planeHeight, planeWidth);

        // add children in AnchorPane
        plane.getChildren().add(bird);
        plane.getChildren().add(score);

        // creating scene
        scene = new Scene(plane, 400, 600);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                birdComponent.fly();
                accelerationTime = 0;
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                String exitMessage = "EXIT";
                socketClient.sendMessage(exitMessage);
            }
        });

        // setting stage
        primaryStage.setTitle("DO IT");
        primaryStage.setScene(scene);
        primaryStage.show();

        // sending message READY TO PLAY
        String connectMessage = "START";
        createClientAndSendMessage(connectMessage);
    }

    public void configureMultiplayerGame() {
        // init AnchorPane
        score = new Text();
        // creating birComponent
        bird = new Rectangle(50.0, 50.0);
        bird.setX(50);
        bird.setY(50);
        bird.setFill(Color.ORANGE);
        int jumpHeight = 75;
        UUID randomId = UUID.randomUUID();
        birdComponent = new Bird(randomId, bird, jumpHeight);
        birdList.add(birdComponent);

        // init attributes for handler
        double planeHeight = 600;
        double planeWidth = 400;
        //init handler for Obstacles (Rectangles)
        obstaclesHandler = new ObstaclesHandler(plane, planeHeight, planeWidth);

        // add children in AnchorPane
        plane.getChildren().add(bird);
        plane.getChildren().add(score);

        // creating scene
        scene = new Scene(plane, 400, 600);
        // bird doing jump if KeyCode == SPACE
        // think what to do if KeyCode == ESCAPE
        // for example, menu with some information
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                birdComponent.fly();
                accelerationTime = 0;
            }
        });

        // setting stage
        primaryStage.setTitle("DO IT");
        primaryStage.setScene(scene);
        primaryStage.show();

        // sending message READY TO PLAY
        String connectMessage = "START "
                + birdComponent.getRandomId() + " "
                + birdComponent.getBirdRectangle().getX() + " "
                + birdComponent.getBirdRectangle().getY() + " "
                + birdComponent.getJumpHeight();
        createMultipleClientsAndSendMessage(connectMessage);
    }

    public void startGame() {
        String conditionMessage = "";
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update(conditionMessage);
            }
        };

        load();

        gameLoop.start();
    }

    public void update(String conditionMessage) {
        gameTime++;
        accelerationTime++;
        double yDelta = 0.02;
        birdComponent.moveBirdY(yDelta * accelerationTime);

        obstaclesHandler.moveObstacles(obstacles);
        if (gameTime % 500 == 0) {
            conditionMessage = String.format("PLAYER_COORDINATES %s %s %s %s", birdComponent.getRandomId(),
                    birdComponent.getBirdRectangle().getX(),
                    birdComponent.getBirdRectangle().getY(),
                    birdComponent.getJumpHeight()
            );
            socketClient.sendMessage(conditionMessage);
            conditionMessage = "CREATE_OBSTACLES";
            socketClient.sendMessage(conditionMessage);
        }

        if (birdComponent.isBirdDead(obstacles, plane)) {
            conditionMessage = "RESTART_GAME";
            socketClient.sendMessage(conditionMessage);
        }
    }

    public void processObstacles() {
        obstacles.addAll(obstaclesHandler.createObstacles());
    }
    public void load() {
        obstacles.addAll(obstaclesHandler.createObstacles());
    }
    public void resetGame() {
        bird.setY(0);
        plane.getChildren().removeAll(obstacles);
        obstacles.clear();
        gameTime = 0;
        accelerationTime = 0;
    }

    public void stopGame() {
        String exitMessage = "EXIT";
        socketClient.sendMessage(exitMessage);
        plane.getChildren().removeAll();
        gameLoop.stop();
        socketClient.stop();
    }

    public void handOverCoordinates(Bird birdComponent) {
        boolean isEnable = false;
        for (Shadow shadow : shadowList) {
            if (shadow.getUUID() == birdComponent.getRandomId()) isEnable = true; break;
        }
        if (!isEnable) {
            Platform.runLater(() -> {
                Shadow shadow = new Shadow(
                        birdComponent.getRandomId(),
                        birdComponent.getBirdRectangle(),
                        birdComponent.getJumpHeight());
                shadowList.add(shadow);
                Rectangle shadowRectangle = birdComponent.getBirdRectangle();
                shadowRectangle.setFill(Color.BLACK);
                shadowRectangle.setOpacity(0.1);
                plane.getChildren().add(shadowRectangle);
            });
        }
    }

    public void moveShadow(Bird birdComponent) {
        boolean isEnable = false;
        for (Shadow shadow : shadowList) {
            if (shadow.getUUID() == birdComponent.getRandomId()) {
                for (Bird otherBird : birdList) {
                    if (otherBird.getRandomId() != birdComponent.getRandomId()) {
                        plane.getChildren().add(shadow);
                        Platform.runLater(shadow::fly);
                        isEnable = true;
                    }
                }
            }
        }
        if (!isEnable) handOverCoordinates(birdComponent);
    }

    public static Controller getInstance() {
        return mainController;
    }

    public void setStage(Stage stage) {
        Controller.primaryStage = stage;
    }

    public void soloGame() {
        configureSoloGame();
    }

    public void multiplayerGame() {
        configureMultiplayerGame();
    }
}
