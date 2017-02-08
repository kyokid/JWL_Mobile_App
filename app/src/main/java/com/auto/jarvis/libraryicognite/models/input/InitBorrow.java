package com.auto.jarvis.libraryicognite.models.input;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HaVH on 2/3/17.
 */

public class InitBorrow implements Parcelable{
    private String userId;
    private String ibeaconId;

    public InitBorrow(String userId, String ibeaconId) {
        this.userId = userId;
        this.ibeaconId = ibeaconId;
    }


    protected InitBorrow(Parcel in) {
        userId = in.readString();
        ibeaconId = in.readString();
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
        parcel.writeString(userId);
        parcel.writeString(ibeaconId);
    }
}
