package com.example.beauty;

public class BeautyItem {
    private String name;
    private int like;
    private String image;
    private boolean liked;

    public BeautyItem(String name, int like, String image) {
        this.name = name;
        this.like = like;
        this.image = image;
        this.liked = false; // 初始化為未按讚
    }

    public String getName() {
        return name;
    }

    public int getLike() {
        return like;
    }

    public String getImage() {
        return image;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
