package com.auto.jarvis.libraryicognite.models.output;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by thiendn on 08/04/2017.
 */

public class RuleDto {
    @SerializedName("listTypeBook")
    private List<BookTypeDto> listTypeBook;
    @SerializedName("fine_cost")
    private Integer fine_cost;

    public RuleDto(List<BookTypeDto> listTypeBook, Integer fine_cost) {
        this.listTypeBook = listTypeBook;
        this.fine_cost = fine_cost;
    }

    public RuleDto() {
    }

    public List<BookTypeDto> getListTypeBook() {
        return listTypeBook;
    }

    public Integer getFine_cost() {
        return fine_cost;
    }
}
