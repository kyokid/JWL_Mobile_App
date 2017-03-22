package com.auto.jarvis.libraryicognite.models.output;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HaVH on 3/21/17.
 */

public class BookAuthorDto implements Parcelable{
    @SerializedName("id") private Integer id;
    @SerializedName("authorName") private String authorName;

    public BookAuthorDto() {

    }


    protected BookAuthorDto(Parcel in) {
        authorName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authorName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookAuthorDto> CREATOR = new Creator<BookAuthorDto>() {
        @Override
        public BookAuthorDto createFromParcel(Parcel in) {
            return new BookAuthorDto(in);
        }

        @Override
        public BookAuthorDto[] newArray(int size) {
            return new BookAuthorDto[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }
}
