
package com.auto.jarvis.libraryicognite.models.output;

import com.google.gson.annotations.SerializedName;

public class BorrowedBookCopy {

    @SerializedName("accountUserId")
    private String mAccountUserId;
    @SerializedName("bookCopyBookPublisher")
    private String mBookCopyBookPublisher;
    @SerializedName("bookCopyBookTitle")
    private String mBookCopyBookTitle;
    @SerializedName("bookCopyRfid")
    private String mBookCopyRfid;
    @SerializedName("borrowLimitDays")
    private Object mBorrowLimitDays;
    @SerializedName("borrowedDate")
    private String mBorrowedDate;
    @SerializedName("daysPerExtend")
    private Object mDaysPerExtend;
    @SerializedName("deadlineDate")
    private String mDeadlineDate;
    @SerializedName("extendNumber")
    private Long mExtendNumber;
    @SerializedName("extendTimes")
    private Object mExtendTimes;
    @SerializedName("extendTimesLimit")
    private Object mExtendTimesLimit;
    @SerializedName("id")
    private Long mId;
    @SerializedName("returnDate")
    private Object mReturnDate;
    @SerializedName("rootId")
    private Object mRootId;

    public String getAccountUserId() {
        return mAccountUserId;
    }

    public String getBookCopyBookPublisher() {
        return mBookCopyBookPublisher;
    }

    public String getBookCopyBookTitle() {
        return mBookCopyBookTitle;
    }

    public String getBookCopyRfid() {
        return mBookCopyRfid;
    }

    public Object getBorrowLimitDays() {
        return mBorrowLimitDays;
    }

    public String getBorrowedDate() {
        return mBorrowedDate;
    }

    public Object getDaysPerExtend() {
        return mDaysPerExtend;
    }

    public String getDeadlineDate() {
        return mDeadlineDate;
    }

    public Long getExtendNumber() {
        return mExtendNumber;
    }

    public Object getExtendTimes() {
        return mExtendTimes;
    }

    public Object getExtendTimesLimit() {
        return mExtendTimesLimit;
    }

    public Long getId() {
        return mId;
    }

    public Object getReturnDate() {
        return mReturnDate;
    }

    public Object getRootId() {
        return mRootId;
    }

    public static class Builder {

        private String mAccountUserId;
        private String mBookCopyBookPublisher;
        private String mBookCopyBookTitle;
        private String mBookCopyRfid;
        private Object mBorrowLimitDays;
        private String mBorrowedDate;
        private Object mDaysPerExtend;
        private String mDeadlineDate;
        private Long mExtendNumber;
        private Object mExtendTimes;
        private Object mExtendTimesLimit;
        private Long mId;
        private Object mReturnDate;
        private Object mRootId;

        public BorrowedBookCopy.Builder withAccountUserId(String accountUserId) {
            mAccountUserId = accountUserId;
            return this;
        }

        public BorrowedBookCopy.Builder withBookCopyBookPublisher(String bookCopyBookPublisher) {
            mBookCopyBookPublisher = bookCopyBookPublisher;
            return this;
        }

        public BorrowedBookCopy.Builder withBookCopyBookTitle(String bookCopyBookTitle) {
            mBookCopyBookTitle = bookCopyBookTitle;
            return this;
        }

        public BorrowedBookCopy.Builder withBookCopyRfid(String bookCopyRfid) {
            mBookCopyRfid = bookCopyRfid;
            return this;
        }

        public BorrowedBookCopy.Builder withBorrowLimitDays(Object borrowLimitDays) {
            mBorrowLimitDays = borrowLimitDays;
            return this;
        }

        public BorrowedBookCopy.Builder withBorrowedDate(String borrowedDate) {
            mBorrowedDate = borrowedDate;
            return this;
        }

        public BorrowedBookCopy.Builder withDaysPerExtend(Object daysPerExtend) {
            mDaysPerExtend = daysPerExtend;
            return this;
        }

        public BorrowedBookCopy.Builder withDeadlineDate(String deadlineDate) {
            mDeadlineDate = deadlineDate;
            return this;
        }

        public BorrowedBookCopy.Builder withExtendNumber(Long extendNumber) {
            mExtendNumber = extendNumber;
            return this;
        }

        public BorrowedBookCopy.Builder withExtendTimes(Object extendTimes) {
            mExtendTimes = extendTimes;
            return this;
        }

        public BorrowedBookCopy.Builder withExtendTimesLimit(Object extendTimesLimit) {
            mExtendTimesLimit = extendTimesLimit;
            return this;
        }

        public BorrowedBookCopy.Builder withId(Long id) {
            mId = id;
            return this;
        }

        public BorrowedBookCopy.Builder withReturnDate(Object returnDate) {
            mReturnDate = returnDate;
            return this;
        }

        public BorrowedBookCopy.Builder withRootId(Object rootId) {
            mRootId = rootId;
            return this;
        }

        public BorrowedBookCopy build() {
            BorrowedBookCopy BorrowedBookCopy = new BorrowedBookCopy();
            BorrowedBookCopy.mAccountUserId = mAccountUserId;
            BorrowedBookCopy.mBookCopyBookPublisher = mBookCopyBookPublisher;
            BorrowedBookCopy.mBookCopyBookTitle = mBookCopyBookTitle;
            BorrowedBookCopy.mBookCopyRfid = mBookCopyRfid;
            BorrowedBookCopy.mBorrowLimitDays = mBorrowLimitDays;
            BorrowedBookCopy.mBorrowedDate = mBorrowedDate;
            BorrowedBookCopy.mDaysPerExtend = mDaysPerExtend;
            BorrowedBookCopy.mDeadlineDate = mDeadlineDate;
            BorrowedBookCopy.mExtendNumber = mExtendNumber;
            BorrowedBookCopy.mExtendTimes = mExtendTimes;
            BorrowedBookCopy.mExtendTimesLimit = mExtendTimesLimit;
            BorrowedBookCopy.mId = mId;
            BorrowedBookCopy.mReturnDate = mReturnDate;
            BorrowedBookCopy.mRootId = mRootId;
            return BorrowedBookCopy;
        }

    }

}
