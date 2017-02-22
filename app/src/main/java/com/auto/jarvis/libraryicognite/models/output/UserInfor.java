package com.auto.jarvis.libraryicognite.models.output;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HaVH on 2/22/17.
 */

public class UserInfor implements Parcelable{
    private String userId;
    private String password;
    private boolean activated;
    private int roleId;
    private boolean inLibrary;

    public UserInfor(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }


    protected UserInfor(Parcel in) {
        userId = in.readString();
        password = in.readString();
        activated = in.readByte() != 0;
        roleId = in.readInt();
        inLibrary = in.readByte() != 0;
    }

    public static final Creator<UserInfor> CREATOR = new Creator<UserInfor>() {
        @Override
        public UserInfor createFromParcel(Parcel in) {
            return new UserInfor(in);
        }

        @Override
        public UserInfor[] newArray(int size) {
            return new UserInfor[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActivated() {
        return activated;
    }

    public boolean isInLibrary() {
        return inLibrary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getRoleId() {
        return roleId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(password);
        parcel.writeByte((byte) (activated ? 1 : 0));
        parcel.writeInt(roleId);
        parcel.writeByte((byte) (inLibrary ? 1 : 0));
    }
}
