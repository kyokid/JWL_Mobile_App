package com.auto.jarvis.libraryicognite.models.input;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HaVH on 1/13/17.
 */

public class User implements Parcelable{
    int id;
    String username;
    String password;
    boolean gender;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        password = in.readString();
        gender = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeByte((byte) (gender ? 1 : 0));
    }
}
