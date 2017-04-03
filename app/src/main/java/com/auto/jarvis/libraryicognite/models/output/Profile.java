
package com.auto.jarvis.libraryicognite.models.output;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("dateOfBirth")
    private String mDateOfBirth;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("fullname")
    private String mFullname;
    @SerializedName("phoneNo")
    private String mPhoneNo;
    @SerializedName("placeOfWork")
    private String mPlaceOfWork;
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("totalBalance")
    private int totalBalance;
    @SerializedName("usableBalance")
    private int usableBalance;

    public int getTotalBalance() {
        return totalBalance;
    }

    public int getUsableBalance() {
        return usableBalance;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getDateOfBirth() {
        return mDateOfBirth;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFullname() {
        return mFullname;
    }

    public String getPhoneNo() {
        return mPhoneNo;
    }

    public String getPlaceOfWork() {
        return mPlaceOfWork;
    }

    public String getUserId() {
        return mUserId;
    }

    public static class Builder {

        private String mAddress;
        private String mDateOfBirth;
        private String mEmail;
        private String mFullname;
        private String mPhoneNo;
        private String mPlaceOfWork;
        private String mUserId;

        public Profile.Builder withAddress(String address) {
            mAddress = address;
            return this;
        }

        public Profile.Builder withDateOfBirth(String dateOfBirth) {
            mDateOfBirth = dateOfBirth;
            return this;
        }

        public Profile.Builder withEmail(String email) {
            mEmail = email;
            return this;
        }

        public Profile.Builder withFullname(String fullname) {
            mFullname = fullname;
            return this;
        }

        public Profile.Builder withPhoneNo(String phoneNo) {
            mPhoneNo = phoneNo;
            return this;
        }

        public Profile.Builder withPlaceOfWork(String placeOfWork) {
            mPlaceOfWork = placeOfWork;
            return this;
        }

        public Profile.Builder withUserId(String userId) {
            mUserId = userId;
            return this;
        }

        public Profile build() {
            Profile Profile = new Profile();
            Profile.mAddress = mAddress;
            Profile.mDateOfBirth = mDateOfBirth;
            Profile.mEmail = mEmail;
            Profile.mFullname = mFullname;
            Profile.mPhoneNo = mPhoneNo;
            Profile.mPlaceOfWork = mPlaceOfWork;
            Profile.mUserId = mUserId;
            return Profile;
        }

    }

}
