package com.example.myapplication;

public class PostModel {
    private String Nid;
    private String message;
    private String nickname;
    private String message_like;

    public PostModel(String Nid, String message, String nickname, String message_like) {
        this.Nid = Nid;
        this.message = message;
        this.nickname = nickname;
        this.message_like = message_like;
    }

    public String getId() {
        return Nid;
    }
    public void setId(String Nid) {
        this.Nid = Nid;
    }

    public String getMessage() {
        return message;
    }
    public void getMessage(String title) {
        this.message = title;
    }

    public String getNickname() {
        return nickname;
    }
    public void getNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessageLike() {
        return message_like;
    }
    public void getMessageLike(String message_like) {
        this.message_like = message_like;
    }
}
