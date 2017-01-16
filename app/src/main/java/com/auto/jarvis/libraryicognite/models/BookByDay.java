package com.auto.jarvis.libraryicognite.models;

import java.util.List;

/**
 * Created by Nguyen.D.Hoang on 1/16/2017.
 */

public class BookByDay {
    private String date;
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public String getDate() {

        return date;
    }

    public BookByDay(String date, List<Book> books) {

        this.date = date;
        this.books = books;
    }
}
