package com.example.egida;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Keys {

        private static Map<String, Pair<String, String>> keys;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Keys() throws IOException {
            String dirPath = "/data/data/com.example.egida/encrypted_files/";
            File encryptedFiles = new File(dirPath);
            if (!encryptedFiles.exists())
                encryptedFiles.mkdirs();
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
            File file = new File("/data/data/com.example.egida/encrypted_files/", "keys.json");
            Gson gson = new Gson();
            String json = gson.toJson(keys);
            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                stream.write(json.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void recoverFromJson() throws IOException {
            File file = new File("/data/data/com.example.egida/encrypted_files/", "keys.json");
            Gson gson = new Gson();
            byte[] encoded = Files.readAllBytes(Paths.get("/data/data/com.example.egida/encrypted_files/keys.json"));
            String json = new String(encoded, StandardCharsets.US_ASCII);
            keys = gson.fromJson(json, HashMap.class);
        }

        public void setValues(String name, String key, String extension) {
            keys.put(name, new Pair<>(key, extension));
            saveToJson();
        }

        public String getDecipherKey(String name, String key) {
            return keys.get(name).first;
        }

        public String getDecipherExtension(String name, String key) {
            return keys.get(name).second;
        }

}