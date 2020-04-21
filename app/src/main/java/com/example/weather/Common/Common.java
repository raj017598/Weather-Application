package com.example.weather.Common;
import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by reale on 05/10/2016.
 */

public class Common {
    public static String API_KEY = "d42d9bef55483c183e2f271b459d6f26";
    public static String API_LINK = "https://api.openweathermap.org/data/2.5/weather";

    @NonNull
    public static String apiRequest(String loc){
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(String.format("?q=%s&apid=%s&units=metrics&mode=json",loc,API_KEY));
        return sb.toString();
    }

    public static String unixTimeStampToDateTime(double unixTimeStamp){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        date.setTime((long)unixTimeStamp*1000);
        return dateFormat.format(date);
    }

    public static String getImage(String icon){
        return String.format("http://openweathermap.org/img/w/%s.png",icon);
    }

    public static String getDateNow(){
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
