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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(bookCopyRfid);
        parcel.writeString(accountUserId);
        parcel.writeInt(borrowLimitDays);
        parcel.writeInt(extendTimes);
        parcel.writeInt(extendTimesLimit);
        parcel.writeInt(daysPerExtend);
        parcel.writeString(borrowedDate);
        parcel.writeString(returnDate);
        parcel.writeString(deadlineDate);
        parcel.writeInt(extendNumber);
        parcel.writeInt(rootId);
    }
}
