package com.example.beauty;

public class BeautyItem {

    private String name;

    private String image;

    private int like;

    public BeautyItem(String name, String image, int like) {
        this.name = name;
        this.image = image;
        this.like = like;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getLike() {
        return like;
    }
}
