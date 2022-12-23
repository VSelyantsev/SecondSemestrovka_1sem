package ru.kpfu.itis.selyantsev.server;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import ru.kpfu.itis.selyantsev.controller.Controller;
import ru.kpfu.itis.selyantsev.model.Bird;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class SocketClient extends Thread {

    private Socket client;
    private Controller controller;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    public SocketClient(String host, int port) {
        try {
            client = new Socket(host, port);
            printWriter = new PrintWriter(client.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void sendMessage(String message) {
        printWriter.println(message);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String futureMessage = bufferedReader.readLine();

                if (futureMessage != null) {
                    String[] messageArray = futureMessage.split(" ");
                    if (messageArray[0].equals("PLAYER_COORDINATES")) {
                        System.out.println(futureMessage);
                        UUID birdUUID = UUID.fromString(messageArray[1]);
                        Rectangle bird = new Rectangle(
                                Double.parseDouble(messageArray[2]),
                                Double.parseDouble(messageArray[3])
                        );
                        int jumpHeight = Integer.parseInt(messageArray[4]);
                        Bird birdComponent = new Bird(
                            birdUUID,
                            bird,
                            jumpHeight
                        );
                        controller.moveShadow(birdComponent);
                    }

                    if (messageArray[0].equals("START")) {
                        System.out.println(futureMessage);
                        Platform.runLater(() -> controller.startGame());
                    }

                    if (messageArray[0].equals("CREATE_OBSTACLES")) {
                        System.out.println(futureMessage);
                        Platform.runLater(() -> controller.processObstacles());
                    }

                    if (messageArray[0].equals("RESTART_GAME")) {
                        System.out.println(futureMessage);
                        Platform.runLater(() -> controller.resetGame());
                    }

                    if (messageArray[0].equals("EXIT")) {
                        System.out.println(futureMessage);
                        Platform.runLater(() -> {
                            controller.stopGame();
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }

}
