package com.auto.jarvis.libraryicognite.models;

/**
 * Created by Nguyen.D.Hoang on 1/16/2017.
 */

public class Book {
    private String title;
    private String author;
    private int id;

    public Book(String title, String author, int id) {
        this.title = title;
        this.author = author;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getId() {
        return id;
    }
}
