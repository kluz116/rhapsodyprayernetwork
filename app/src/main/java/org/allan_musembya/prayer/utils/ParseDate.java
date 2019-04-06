package org.allan_musembya.prayer.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kluz on 4/25/18.
 */

public class ParseDate {
    public static String getDate;
    static String  currentDateandTime;

    public static  String  getDateParsed(String pasedDated){

        try {
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date convertedDate = dateFormat.parse(pasedDated);

            CharSequence relavetime1 = DateUtils.getRelativeTimeSpanString(
                    convertedDate.getTime(),
                    now,
                    DateUtils.SECOND_IN_MILLIS);
            getDate = relavetime1.toString();

        }catch(ParseException e) {
            e.printStackTrace();
        }

        return getDate;
    }

    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateandTime = sdf.format(new Date());

        return  currentDateandTime;
    }

    public static boolean getDayOfMonth(){
        String get_compare_day = "01";
        boolean status;
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        currentDateandTime = sdf.format(new Date());
        status = currentDateandTime.equals(get_compare_day);
        return  status;
    }
}
