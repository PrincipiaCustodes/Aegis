package com.example.egida;

import com.google.gson.annotations.SerializedName;

public class JsonFile {

    /*@SerializedName("name")
    private String name;

    @SerializedName("key")
    private String key;

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }*/

    // example

    private int userId;

    private int id;

    private String title;

    @SerializedName("body")
    private String text;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
