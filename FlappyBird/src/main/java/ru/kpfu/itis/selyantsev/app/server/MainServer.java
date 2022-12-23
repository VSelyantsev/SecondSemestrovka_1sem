package ru.kpfu.itis.selyantsev.app.server;

import ru.kpfu.itis.selyantsev.server.SocketServer;

public class MainServer {
    public static void main(String[] args) {
        SocketServer server = SocketServer.getInstance();
        server.start(7777);
    }
}
