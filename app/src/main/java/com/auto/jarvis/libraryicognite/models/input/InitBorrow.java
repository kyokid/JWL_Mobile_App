package com.auto.jarvis.libraryicognite.models.input;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HaVH on 2/3/17.
 */

public class InitBorrow implements Parcelable{
    private User user;
    private String iBeaconMacAddress;

    protected InitBorrow(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        iBeaconMacAddress = in.readString();
    }

    public static final Creator<InitBorrow> CREATOR = new Creator<InitBorrow>() {
        @Override
        public InitBorrow createFromParcel(Parcel in) {
            return new InitBorrow(in);
        }

        @Override
        public InitBorrow[] newArray(int size) {
            return new InitBorrow[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(user, i);
        parcel.writeString(iBeaconMacAddress);
    }
}
