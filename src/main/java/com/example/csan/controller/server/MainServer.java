package com.example.csan.controller.server;

import java.net.SocketException;

public class MainServer {

    public static void main(String[] args) throws SocketException {
        PingServer pingServer = new PingServer();
        Thread serverThread = new Thread(pingServer);
        serverThread.start();
    }
}
