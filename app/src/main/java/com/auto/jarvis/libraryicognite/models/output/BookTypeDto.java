package com.auto.jarvis.libraryicognite.models.output;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thiendn on 08/04/2017.
 */

public class BookTypeDto {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("borrowLimitDays")
    private Integer borrowLimitDays;
    @SerializedName("daysPerExtend")
    private Integer daysPerExtend;
    @SerializedName("extendTimesLimit")
    private Integer extendTimesLimit;
    @SerializedName("lateDaysLimit")
    private Integer lateDaysLimit;

    public BookTypeDto() {
    }

    public BookTypeDto(Integer id, String name, Integer borrowLimitDays, Integer daysPerExtend, Integer extendTimesLimit, Integer lateDaysLimit) {
        this.id = id;
        this.name = name;
        this.borrowLimitDays = borrowLimitDays;
        this.daysPerExtend = daysPerExtend;
        this.extendTimesLimit = extendTimesLimit;
        this.lateDaysLimit = lateDaysLimit;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getBorrowLimitDays() {
        return borrowLimitDays;
    }

    public Integer getDaysPerExtend() {
        return daysPerExtend;
    }

    public Integer getExtendTimesLimit() {
        return extendTimesLimit;
    }

    public Integer getLateDaysLimit() {
        return lateDaysLimit;
    }
}
