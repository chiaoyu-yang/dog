package com.example.soulgo;

public class BookModel {
    private String id;
    private String name;
    private String image;

    public BookModel() {
        // 空的建構子可供 Firebase Realtime Database 使用
    }

    public BookModel(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
