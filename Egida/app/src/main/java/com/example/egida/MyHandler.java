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

    private static final String NOT_FOUND_MESSAGE = "NOT FOUND";

    private static final Map<String, String> CONTENT_TYPES = new HashMap<String, String>() {{
        put("jpg", "image/jpeg");
        put("html", "text/html");
        put("json", "application/json");
        put("txt", "text/plain");
        put("", "text/plain");
    }};

    private final Socket socket;
    private final String directory;

    MyHandler(Socket socket, String directory) {
        this.socket = socket;
        this.directory = directory;
    }

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

                this.responseHeader(output, 200, "OK", type, fileBytes.length);
                output.write(fileBytes);
            } else {
                String type = CONTENT_TYPES.get("text");
                this.responseHeader(output, 404, "Not Found", type, NOT_FOUND_MESSAGE.length());
                output.write(NOT_FOUND_MESSAGE.getBytes());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // получаем ссылку на файл из запроса
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

        if(extensionStart == -1){
            return "";
        } else {
            return name.substring(extensionStart + 1);
        }
    }

    // формирование заголовка ответа
    private void responseHeader(OutputStream output, int statusCode, String statusText, String type, long length) {
        PrintStream printStream = new PrintStream(output);
        printStream.printf("HTTP/1.1 %s %s%n", statusCode, statusText);
        printStream.printf("Content-Type: %s%n", type);
        printStream.printf("Content-Length: %s%n%n", length);
    }
}
