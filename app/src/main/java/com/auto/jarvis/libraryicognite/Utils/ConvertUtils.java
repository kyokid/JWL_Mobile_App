package com.auto.jarvis.libraryicognite.Utils;

import android.util.Log;

import com.auto.jarvis.libraryicognite.interfaces.ApiInterface;
import com.auto.jarvis.libraryicognite.models.output.RestService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    public static String formateDate(String strSource) {
        String strDestination = "";
        try {
            SimpleDateFormat formatSource = new SimpleDateFormat("yyyy-MM-dd");

            Date date = formatSource.parse(strSource);
            SimpleDateFormat formatDestination = new SimpleDateFormat("dd/MM/yyyy");

            return strDestination = formatDestination.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDestination;
    }

}
