package com.example.soulgo.Book;

public class BookModel {
    private String Bid;
    private String name;
    private String image;

    public BookModel(String Bid, String name, String image) {
        this.Bid = Bid;
        this.name = name;
        this.image = image;
    }

    public String getId() {
        return Bid;
    }

    public void setId(String Bid) {
        this.Bid = Bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
