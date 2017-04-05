
package com.auto.jarvis.libraryicognite.models.output;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("activated")
    private Boolean mActivated;
    @SerializedName("borrowedBookCopies")
    private List<BorrowedBookCopy> mBorrowedBookCopies;
    @SerializedName("inLibrary")
    private Boolean mInLibrary;
    @SerializedName("profile")
    private Profile mProfile;
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("totalBalance")
    private int totalBalance;
    @SerializedName("usableBalance")
    private int usableBalance;

    public Boolean getActivated() {
        return mActivated;
    }

    public List<BorrowedBookCopy> getBorrowedBookCopies() {
        return mBorrowedBookCopies;
    }

    public Boolean getInLibrary() {
        return mInLibrary;
    }

    public Profile getProfile() {
        return mProfile;
    }

    public String getUserId() {
        return mUserId;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public int getUsableBalance() {
        return usableBalance;
    }

    public static class Builder {

        private Boolean mActivated;
        private List<BorrowedBookCopy> mBorrowedBookCopies;
        private Boolean mInLibrary;
        private Profile mProfile;
        private String mUserId;

        public Data.Builder withActivated(Boolean activated) {
            mActivated = activated;
            return this;
        }

        public Data.Builder withBorrowedBookCopies(List<BorrowedBookCopy> borrowedBookCopies) {
            mBorrowedBookCopies = borrowedBookCopies;
            return this;
        }

        public Data.Builder withInLibrary(Boolean inLibrary) {
            mInLibrary = inLibrary;
            return this;
        }

        public Data.Builder withProfile(Profile profile) {
            mProfile = profile;
            return this;
        }

        public Data.Builder withUserId(String userId) {
            mUserId = userId;
            return this;
        }

        public Data build() {
            Data Data = new Data();
            Data.mActivated = mActivated;
            Data.mBorrowedBookCopies = mBorrowedBookCopies;
            Data.mInLibrary = mInLibrary;
            Data.mProfile = mProfile;
            Data.mUserId = mUserId;
            return Data;
        }

    }

}
