package com.example.egida;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private boolean exit;
    private final int port;
    private final String directory;

    public Server(int port, String directory) {
        this.port = port;
        this.directory = directory;
    }

    @Override
    public void run() {
        while(!exit) {
            try (ServerSocket server = new ServerSocket(this.port)) {
                while (true) {
                    Socket socket = server.accept();
                    Thread thread = new Thread(new MyHandler(socket, this.directory));
                    thread.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopServer()
    {
        exit = true;
    }
}