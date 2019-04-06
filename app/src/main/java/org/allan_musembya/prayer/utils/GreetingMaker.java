package org.allan_musembya.prayer.utils;

import java.util.Calendar;


/**
 * Created by kluz on 4/25/18.
 */


public class GreetingMaker {

    public static String responseMessage;

    public static String getMessage(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            responseMessage = "Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            responseMessage= "Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            responseMessage = "Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            responseMessage = "Good Night";
        }

    return responseMessage;
    }

}

