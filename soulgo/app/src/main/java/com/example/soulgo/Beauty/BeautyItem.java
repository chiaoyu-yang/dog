package com.example.soulgo.Beauty;

public class BeautyItem {
    private String name, image;
    private int like, beauty_id;
    private boolean liked;

    public BeautyItem(String name, int like, String image, int beauty_id) {
        this.name = name;
        this.like = like;
        this.image = image;
        this.beauty_id = beauty_id;
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

    public int getBeautyId() { return beauty_id; }

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