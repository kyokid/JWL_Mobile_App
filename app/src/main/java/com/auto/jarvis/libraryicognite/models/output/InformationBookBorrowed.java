package com.auto.jarvis.libraryicognite.models.output;

import android.os.Parcel;
import android.os.Parcelable;

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
    private String bookCopyBookTitle;
    private String bookCopyBookPublisher;
    private String bookCopyBookDescription;
    private int bookCopyBookNumberOfPages;
    private int bookCopyBookPublishYear;
    private int bookCopyBookPrice;
    private String bookCopyBookThumbnail;


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
        bookCopyBookTitle = in.readString();
        bookCopyBookPublisher = in.readString();
        bookCopyBookDescription = in.readString();
        bookCopyBookNumberOfPages = in.readInt();
        bookCopyBookPublishYear = in.readInt();
        bookCopyBookPrice = in.readInt();
        bookCopyBookThumbnail = in.readString();
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
        dest.writeString(bookCopyBookTitle);
        dest.writeString(bookCopyBookPublisher);
        dest.writeString(bookCopyBookDescription);
        dest.writeInt(bookCopyBookNumberOfPages);
        dest.writeInt(bookCopyBookPublishYear);
        dest.writeInt(bookCopyBookPrice);
        dest.writeString(bookCopyBookThumbnail);
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

    public int getBookCopyBookPrice() {
        return bookCopyBookPrice;
    }

    public String getBookCopyBookThumbnail() {
        return bookCopyBookThumbnail;
    }

    public int getBookCopyBookPublishYear() {
        return bookCopyBookPublishYear;
    }

    public Integer getBookCopyBookNumberOfPages() {
        return bookCopyBookNumberOfPages;
    }

    public String getBookCopyBookDescription() {
        return bookCopyBookDescription;
    }

    public String getBookCopyBookPublisher() {
        return bookCopyBookPublisher;
    }

    public String getBookCopyBookTitle() {
        return bookCopyBookTitle;
    }

    public void setBookCopyBookTitle(String bookCopyBookTitle) {
        this.bookCopyBookTitle = bookCopyBookTitle;
    }

    public void setBookCopyBookPublisher(String bookCopyBookPublisher) {
        this.bookCopyBookPublisher = bookCopyBookPublisher;
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
