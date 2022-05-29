package com.example.egida;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private static int port;
    private boolean exit;
    private final String directory;

    public Server(String directory) {
        this.directory = directory;
    }

    public static int getPort(){
        return port;
    }

    @Override
    public void run() {
        while(!exit) {
            try (ServerSocket server = new ServerSocket(0)) {
                port = server.getLocalPort();
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