package com.example.egida;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class FilesInfo {

    static class File {
        private int id;
        private String name;
        private String extension;
        private long size;
        private String key;
        private String date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public void toJson(File file) throws IOException {
        Gson gson = new Gson();
        java.io.File jsonFile = new java.io.File(Check.filesInfoPath + file.getName() + ".json");
        FileOutputStream fos = new FileOutputStream(jsonFile);

        fos.write((gson.toJson(file)).getBytes());
        fos.close();
    }

    public File fromJson(java.io.File jsonFile) throws IOException {
        Gson gson = new Gson();
        FileInputStream fis = new FileInputStream(jsonFile);
        Scanner scanner = new Scanner(fis);
        String json = scanner.nextLine();
        fis.close();

        return gson.fromJson(json, File.class);
    }
}
