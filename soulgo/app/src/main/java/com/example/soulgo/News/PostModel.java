package com.example.soulgo.News;

public class PostModel {
    private String Nid;
    private String message;
    private String nickname;
    private String message_like;
    private String message_id;
    private boolean isLiked;

    public String getNid() {
        return Nid;
    }

    public void setNid(String Nid) {
        this.Nid = Nid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessageLike() {
        return message_like;
    }

    public void setMessageLike(String message_like) {
        this.message_like = message_like;
    }

    public String getMessageId() {
        return message_id;
    }

    public void getMessageId(String message_id) {
        this.message_id = message_id;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
