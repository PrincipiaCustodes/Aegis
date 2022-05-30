package com.example.egida;

import android.util.Pair;

import java.io.File;
import java.util.Map;

public class Keys {

    public class Data {
        private Map<String, Pair<String, String>> keys;

        public Data() {
            String dirPath = "/data/data/com.example.egida/encrypted_files/";
            File endryptedFiles = new File(dirPath);
            if (!endryptedFiles.exists())
                endryptedFiles.mkdirs();
            File file = new File("/data/data/com.example.egida/encrypted_files/", "keys.json");
            if (!file.exists()) {
                
            }
        }

        public void setValues(String name, String key, String extension) {
            keys.put(name, new Pair<>(key, extension));
        }

        public String getDecipherKey(String name, String key) {
            return keys.get(name).first;
        }

        public String getDecipherExtension(String name, String key) {
            return keys.get(name).second;
        }
    }


}