package com.auto.jarvis.libraryicognite.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.auto.jarvis.libraryicognite.models.output.InformationBookBorrowed;

/**
 * Created by Havh on 1/16/2017.
 */

public class Book implements Parcelable{
    private String rfidBook;
    private String title;
    private String publisher;
    private String author;
    private String deadLine;
    private String description;
    private int numberOfPages;
    private int id;
    private int publishYear;
    private int price;
    private String thumbnail;

    public Book() {

    }

    public Book(String title, String author, int id) {
        this.title = title;
        this.author = author;
        this.id = id;
    }


    protected Book(Parcel in) {
        rfidBook = in.readString();
        title = in.readString();
        publisher = in.readString();
        author = in.readString();
        deadLine = in.readString();
        description = in.readString();
        numberOfPages = in.readInt();
        id = in.readInt();
        publishYear = in.readInt();
        price = in.readInt();
        thumbnail = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getPrice() {
        return price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public String getRfidBook() {
        return rfidBook;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public String getDescription() {
        return description;
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

    public static Book fromBorrowedList(InformationBookBorrowed bookBorrowed) {
        Book book = new Book();
        book.title = bookBorrowed.getBookCopyBookTitle();
        book.publisher = bookBorrowed.getBookCopyBookPublisher();
        book.deadLine = bookBorrowed.getDeadlineDate();
        book.rfidBook = bookBorrowed.getBookCopyRfid();
        book.numberOfPages = bookBorrowed.getBookCopyBookNumberOfPages();
        book.description = bookBorrowed.getBookCopyBookDescription();
        book.publishYear = bookBorrowed.getBookCopyBookPublishYear();
        book.thumbnail = bookBorrowed.getBookCopyBookThumbnail();
        book.price = bookBorrowed.getBookCopyBookPrice();
        return book;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rfidBook);
        dest.writeString(title);
        dest.writeString(publisher);
        dest.writeString(author);
        dest.writeString(deadLine);
        dest.writeString(description);
        dest.writeInt(numberOfPages);
        dest.writeInt(id);
        dest.writeInt(publishYear);
        dest.writeInt(price);
        dest.writeString(thumbnail);
    }
}
