package com.auto.jarvis.libraryicognite.models;

import java.util.List;

/**
 * Created by Havh on 1/16/2017.
 */

public class BookByDay {
    private String borrowedDate;
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public String getBorrowedDate() {

        return borrowedDate;
    }

    public BookByDay(String borrowedDate, List<Book> books) {

        this.borrowedDate = borrowedDate;
        this.books = books;
    }
}
