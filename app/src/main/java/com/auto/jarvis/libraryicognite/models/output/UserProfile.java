package com.auto.jarvis.libraryicognite.models.output;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HaVH on 3/8/17.
 */

public class UserProfile implements Parcelable{


    private String userId;
    private String fullname;
    private String email;
    private String address;
    private String dateOfBirth;
    private String phoneNo;
    private String placeOfWork;


    public String getUserId() {
        return userId;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    protected UserProfile(Parcel in) {
        userId = in.readString();
        fullname = in.readString();
        email = in.readString();
        address = in.readString();
        dateOfBirth = in.readString();
        phoneNo = in.readString();
        placeOfWork = in.readString();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(fullname);
        parcel.writeString(email);
        parcel.writeString(address);
        parcel.writeString(dateOfBirth);
        parcel.writeString(phoneNo);
        parcel.writeString(placeOfWork);
    }
}
