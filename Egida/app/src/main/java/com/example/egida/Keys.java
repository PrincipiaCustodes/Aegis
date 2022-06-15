package com.example.egida;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Keys {

    private static Map<String, String> keys;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Keys() throws IOException {
        String dirPath = "/data/data/com.example.egida/app_files/";
        File appFiles = new File(dirPath);
        if (!appFiles.exists())
            appFiles.mkdirs();
        File encryptedFiles = new File("/data/data/com.example.egida/encrypted_files/");
        if (!encryptedFiles.exists())
            encryptedFiles.mkdirs();
        File tempFiles = new File("/data/data/com.example.egida/temp_files/");
        if (!tempFiles.exists())
            tempFiles.mkdirs();
        File sharedFiles = new File("/data/data/com.example.egida/shared_files/");
        if (!sharedFiles.exists())
            sharedFiles.mkdirs();

        File file = new File(dirPath, "keys.json");
        keys = new HashMap<>();
        if (file.exists()) {
            recoverFromJson();
        }
        else {
            saveToJson();
        }
    }

    public void saveToJson() {
        File file = new File("/data/data/com.example.egida/app_files/", "keys.json");
        Gson gson = new Gson();
        String json = gson.toJson(keys);
        try {
            AesEncoder.writeBytesToFile("/data/data/com.example.egida/app_files/keys.json", json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        @RequiresApi(api = Build.VERSION_CODES.O)
    public void recoverFromJson() throws IOException {
        Gson gson = new Gson();
        File keysFile = new File("/data/data/com.example.egida/app_files/keys.json");
        //byte[] encoded = Files.readAllBytes(Paths.get(("/data/data/com.example.egida/app_files/keys.json")));
            // byte[] encoded = Files.readAllBytes(new File("/data/data/com.example.egida/app_files/keys.json").toPath());
            FileInputStream fis = new FileInputStream(keysFile);
        //String json = new String(encoded, StandardCharsets.US_ASCII);
            Scanner scanner = new Scanner(fis);
            String json = scanner.nextLine();
        keys = gson.fromJson(json, HashMap.class);
    }

    public void setValues(String name, String key) {
        keys.put(name, key);
        saveToJson();
    }

    public static String getDecipherKey(String name) {
        return keys.get(name);
    }

    public  String getDecipherExtension(String name) {
        return keys.get(name);
    }

    public void remove(String name) {
        keys.remove(name);
        saveToJson();
    }

    static String getFileNameWithoutExtension(File file) {
        String fileName = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                fileName = name.replaceFirst("[.][^.]+$", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileName = "";
        }

        return fileName;
    }
}