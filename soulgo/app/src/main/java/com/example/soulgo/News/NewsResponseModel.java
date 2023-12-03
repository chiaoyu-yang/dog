package com.example.soulgo.News;

import java.util.List;

public class NewsResponseModel {
    private boolean error;
    private String message;
    private List<NewsModel> data;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<NewsModel> getData() {
        return data;
    }
}

