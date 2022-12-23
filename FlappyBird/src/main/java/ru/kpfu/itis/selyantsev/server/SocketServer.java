package ru.kpfu.itis.selyantsev.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SocketServer {
    private final List<Client> clients = new CopyOnWriteArrayList<>();
    public void start(int port) {
        new Thread() {
            ServerSocket serverSocket;
            {
                try {
                    serverSocket = new ServerSocket(port);

                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        Client client = new Client(clientSocket);
                        clients.add(client);

                        client.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private static SocketServer socketServer;
    public static SocketServer getInstance() {
        if (socketServer == null) {
            socketServer = new SocketServer();
        }
        return socketServer;
    }
    public List<Client> getClients() {
        return clients;
    }

}
