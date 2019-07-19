package com.royce.tripbotify.utils;

import android.annotation.SuppressLint;

import com.amadeus.resources.Period;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getBusiestPeriodString(String city, Period[] period) {
        String proTip = "Pro tip : The busiest traveling period to " + city + " in 2018 was ";
        if (period == null || period.length == 0)
            return proTip.concat("June, May and December in that order");
        for (int i = 0; i < period.length; i++) {
            //Log.i(AppConstants.LOG_TAG, period[i].getPeriod());
            proTip = proTip.concat(getMonth(period[i].getPeriod()));
            if (i == 2) {
                proTip = proTip.concat(" in that order");
                break;
            } else
                proTip = proTip.concat(i != 1 ? ", " : " and ");
        }
        return proTip;
    }

    private static String getMonth(String date) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        String m = date.substring(date.indexOf("-") + 1).replace("0", "");
        try {
            return monthNames[Integer.parseInt(m) - 1];
        } catch (NumberFormatException ex) {
            return "July";
        }
    }

    public static String getMonthDay(String date) {
        //2019-08-01
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        String m = date.substring(5, 7).replace("0", "");
        //Log.i(AppConstants.LOG_TAG, "month -> " + m);
        try {
            return monthNames[Integer.parseInt(m) - 1].substring(0, 3) + " " + date.substring(8);
        } catch (NumberFormatException ex) {
            return "July";
        }
    }

    public static String getJustDate(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MMM dd, hh:mm");
        return format.format(date);
    }

    public static String getJustDate(String date) {
       // Log.i(AppConstants.LOG_TAG, date);
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+05:30'", Locale.ENGLISH).parse(date);
            return new SimpleDateFormat("hh:mm").format(d);
        } catch (ParseException e1) {
            return "Time Date Parse Error";
        }
    }
}
