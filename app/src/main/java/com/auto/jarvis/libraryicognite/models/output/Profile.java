
package com.auto.jarvis.libraryicognite.models.output;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntegerRes;

import com.google.gson.annotations.SerializedName;

public class Profile implements Parcelable{

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


    protected Profile(Parcel in) {
        mAddress = in.readString();
        mDateOfBirth = in.readString();
        mEmail = in.readString();
        mFullname = in.readString();
        mPhoneNo = in.readString();
        mPlaceOfWork = in.readString();
        mUserId = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAddress);
        dest.writeString(mDateOfBirth);
        dest.writeString(mEmail);
        dest.writeString(mFullname);
        dest.writeString(mPhoneNo);
        dest.writeString(mPlaceOfWork);
        dest.writeString(mUserId);
    }
}
