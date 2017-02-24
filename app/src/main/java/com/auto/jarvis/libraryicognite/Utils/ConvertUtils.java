package com.auto.jarvis.libraryicognite.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HaVH on 2/24/17.
 */

public class ConvertUtils {

    public static Date convertStringtoDate(String stringDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(stringDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getCurrentDate() {
        Date date = new Date();
        return date;
    }
}
