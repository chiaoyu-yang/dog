package com.example.beauty;

public class TopBeauty {
    private String image;
    private String name;
    private int like;

    // 創建相應的 getter 方法
    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getLike() {
        return like;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
