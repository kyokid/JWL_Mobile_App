package com.auto.jarvis.libraryicognite.models;

import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;

/**
 * Created by Nguyen.D.Hoang on 1/16/2017.
 */

public class Book {
    private String title;
    private String publisher;
    private String author;
    private String deadLine;
    private int id;

    public Book() {

    }

    public Book(String title, String author, int id) {
        this.title = title;
        this.author = author;
        this.id = id;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public String getPublisher() {
        return publisher;
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

    public static Book fromBorrowedList(int autoIncre, InformationBookBorrowed bookBorrowed) {
        Book book = new Book();
        book.title = String.valueOf(autoIncre) + ". " + bookBorrowed.getBookCopyBookTitle();
        book.publisher = bookBorrowed.getBookCopyBookPublisher();
        book.deadLine = bookBorrowed.getDeadlineDate();
        return book;
    }
}
