package com.example.soulgo.News;

import java.util.List;

public class PostResponseModel {
    private boolean error;
    private String message;
    private List<PostModel> data;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<PostModel> getData() {
        return data;
    }
}

