package ru.kpfu.itis.selyantsev.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client extends Thread {
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Socket socket;
    private SocketServer socketServer;
    private boolean running = true;

    private void sendMessageToOthers(String message, boolean isForAll) {
        for (Client other: socketServer.getClients()) {
            if (isForAll || !this.equals(other)){
                other.sendMessage(message);
            }
        }
    }

    public Client (Socket socket) {
        try {
            this.socket = socket;
            socketServer = SocketServer.getInstance();

            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void sendMessage(String message) {
            printWriter.println(message);
    }

    @Override
    public void run() {
        while (running) {
            try {
                String futureMessage = bufferedReader.readLine();
                if (futureMessage != null) {
                    String[] messageArray = futureMessage.split(" ");
                    if (messageArray[0].equals("START")) {
                        sendMessageToOthers(futureMessage, true);
                    }
                    if (messageArray[0].equals("RESTART_GAME")) {
                        sendMessageToOthers(futureMessage, true);
                    }
                    if (messageArray[0].equals("CREATE_OBSTACLES")) {
                        sendMessageToOthers(futureMessage, true);
                    }
                    if (messageArray[0].equals("PLAYER_COORDINATES")) {
                        sendMessageToOthers(futureMessage, true);
                    }
                    if (messageArray[0].equals("EXIT")) {
                        sendMessageToOthers(futureMessage, true);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
