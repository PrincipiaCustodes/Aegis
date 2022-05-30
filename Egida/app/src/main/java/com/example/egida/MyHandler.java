package com.example.egida;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MyHandler implements Runnable {

    private final Socket socket;
    private final String directory;

    private static String NOT_FOUND_MESSAGE = "NOT FOUND";

    MyHandler(Socket socket, String directory) {
        this.socket = socket;
        this.directory = directory;
    }

    private static final Map<String, String> CONTENT_TYPES = new HashMap<String, String>() {{
        put("jpg", "image/jpeg");
        put("html", "text/html");
        put("json", "application/json");
        put("txt", "text/plain");
        put("", "text/plain");
    }};

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {
        try {
            InputStream input = this.socket.getInputStream();
            OutputStream output = this.socket.getOutputStream();
            String url = this.getRequestUrl(input);
            Path filePath = Paths.get(new File(this.directory, url).getAbsolutePath());

            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                Object extension = this.getFileExtension(filePath);
                String type = CONTENT_TYPES.get(extension);

                byte[] fileBytes = Files.readAllBytes(filePath);
                this.sendHeader(output, 200, "OK", type, fileBytes.length);
                output.write(fileBytes);
            } else {
                String type = CONTENT_TYPES.get("text");
                this.sendHeader(output, 404, "Not Found", type, NOT_FOUND_MESSAGE.length());
                output.write(NOT_FOUND_MESSAGE.getBytes());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // получаем ссылку запроса
    private String getRequestUrl(InputStream input) {
        Scanner reader = new Scanner(input).useDelimiter("\r\n");
        String line = reader.next();
        return line.split(" ")[1];
    }

    // получаем расширение файла
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getFileExtension(Path path) {
        String name = path.getFileName().toString();
        int extensionStart = name.lastIndexOf(".");
        return extensionStart == -1 ? "" : name.substring(extensionStart + 1);
    }

    private void sendHeader(OutputStream output, int statusCode, String statusText, String type, long length) {
        PrintStream ps = new PrintStream(output);
        ps.printf("HTTP/1.1 %s %s%n", statusCode, statusText);
        ps.printf("Content-Type: %s%n", type);
        ps.printf("Content-Length: %s%n%n", length);
    }
}
