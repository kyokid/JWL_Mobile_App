package com.auto.jarvis.libraryicognite.models.output;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by HaVH on 2/9/17.
 */

public class InformationBookBorrowed implements Parcelable {
    private int id;
    private String bookCopyRfid;
    private String accountUserId;
    private int borrowLimitDays;
    private int extendTimes;
    private int extendTimesLimit;
    private int daysPerExtend;
    private String borrowedDate;

    private String returnDate;

    private String deadlineDate;
    private int extendNumber;
    private int rootId;
    @SerializedName("bookCopyBookTitle")
    private String bookTitle;

    @SerializedName("bookCopyBookPublisher")
    private String publisher;

    @SerializedName("bookCopyBookDescription")
    private String Description;

    @SerializedName("bookCopyBookNumberOfPages")
    private int numberOfPages;

    @SerializedName("bookCopyBookPublishYear")
    private int publishYear;

    @SerializedName("bookCopyBookPrice")
    private int price;

    @SerializedName("bookCopyBookThumbnail")
    private String thumbnail;

    @SerializedName("bookCopyBookBookAuthors")
    private ArrayList<BookAuthorDto> authors;

    @SerializedName("bookCopyBookBookCategories")
    private ArrayList<BookCategoryDto> categories;

    private boolean deadline;


    protected InformationBookBorrowed(Parcel in) {
        id = in.readInt();
        bookCopyRfid = in.readString();
        accountUserId = in.readString();
        borrowLimitDays = in.readInt();
        extendTimes = in.readInt();
        extendTimesLimit = in.readInt();
        daysPerExtend = in.readInt();
        borrowedDate = in.readString();
        returnDate = in.readString();
        deadlineDate = in.readString();
        extendNumber = in.readInt();
        rootId = in.readInt();
        bookTitle = in.readString();
        publisher = in.readString();
        Description = in.readString();
        numberOfPages = in.readInt();
        publishYear = in.readInt();
        price = in.readInt();
        thumbnail = in.readString();
        authors = in.createTypedArrayList(BookAuthorDto.CREATOR);
        categories = in.createTypedArrayList(BookCategoryDto.CREATOR);
        deadline = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(bookCopyRfid);
        dest.writeString(accountUserId);
        dest.writeInt(borrowLimitDays);
        dest.writeInt(extendTimes);
        dest.writeInt(extendTimesLimit);
        dest.writeInt(daysPerExtend);
        dest.writeString(borrowedDate);
        dest.writeString(returnDate);
        dest.writeString(deadlineDate);
        dest.writeInt(extendNumber);
        dest.writeInt(rootId);
        dest.writeString(bookTitle);
        dest.writeString(publisher);
        dest.writeString(Description);
        dest.writeInt(numberOfPages);
        dest.writeInt(publishYear);
        dest.writeInt(price);
        dest.writeString(thumbnail);
        dest.writeTypedList(authors);
        dest.writeTypedList(categories);
        dest.writeByte((byte) (deadline ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InformationBookBorrowed> CREATOR = new Creator<InformationBookBorrowed>() {
        @Override
        public InformationBookBorrowed createFromParcel(Parcel in) {
            return new InformationBookBorrowed(in);
        }

        @Override
        public InformationBookBorrowed[] newArray(int size) {
            return new InformationBookBorrowed[size];
        }
    };

    public boolean isDeadline() {
        return deadline;
    }

    public void setDeadline(boolean deadline) {
        this.deadline = deadline;
    }

    public ArrayList<BookAuthorDto> getAuthors() {
        return authors;
    }

    public ArrayList<BookCategoryDto> getCategories() {
        return categories;
    }

    public int getPrice() {
        return price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public String getDescription() {
        return Description;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookCopyRfid() {
        return bookCopyRfid;
    }

    public void setBookCopyRfid(String bookCopyRfid) {
        this.bookCopyRfid = bookCopyRfid;
    }

    public String getAccountUserId() {
        return accountUserId;
    }

    public void setAccountUserId(String accountUserId) {
        this.accountUserId = accountUserId;
    }

    public int getBorrowLimitDays() {
        return borrowLimitDays;
    }

    public void setBorrowLimitDays(int borrowLimitDays) {
        this.borrowLimitDays = borrowLimitDays;
    }

    public int getExtendTimes() {
        return extendTimes;
    }

    public void setExtendTimes(int extendTimes) {
        this.extendTimes = extendTimes;
    }

    public int getExtendTimesLimit() {
        return extendTimesLimit;
    }

    public void setExtendTimesLimit(int extendTimesLimit) {
        this.extendTimesLimit = extendTimesLimit;
    }

    public int getDaysPerExtend() {
        return daysPerExtend;
    }

    public void setDaysPerExtend(int daysPerExtend) {
        this.daysPerExtend = daysPerExtend;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public int getExtendNumber() {
        return extendNumber;
    }

    public void setExtendNumber(int extendNumber) {
        this.extendNumber = extendNumber;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }



}
