package com.example.soulgo.Book;

import com.example.soulgo.Book.DetailModel;

import java.util.List;

public class DetailResponseModel {
    private boolean error;
    private String message;
    private List<DetailModel> data;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<DetailModel> getData() {
        return data;
    }


}
