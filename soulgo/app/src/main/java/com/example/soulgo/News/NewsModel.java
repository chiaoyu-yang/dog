package com.example.soulgo.News;

public class NewsModel {
    private String Nid;
    private String title;
    private String content;
    private String image;

    public NewsModel(String Nid, String title, String content, String image) {
        this.Nid = Nid;
        this.title = title;
        this.content = content;
        this.image = image;
    }

    public String getId() {
        return Nid;
    }
    public void setId(String Nid) {
        this.Nid = Nid;
    }

    public String getTitle() {
        return title;
    }
    public void getTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void getContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
