package com.auto.jarvis.libraryicognite.models.output;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HaVH on 3/21/17.
 */

public class BookCategoryDto implements Parcelable{
    private Integer id;
    private String categoryName;


    protected BookCategoryDto(Parcel in) {
        categoryName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookCategoryDto> CREATOR = new Creator<BookCategoryDto>() {
        @Override
        public BookCategoryDto createFromParcel(Parcel in) {
            return new BookCategoryDto(in);
        }

        @Override
        public BookCategoryDto[] newArray(int size) {
            return new BookCategoryDto[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
