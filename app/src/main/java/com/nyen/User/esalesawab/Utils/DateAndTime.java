package com.nyen.User.esalesawab.Utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {

    public static String getDateF(Long currentTime){

        //Date Calculation
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleFormatDate = new SimpleDateFormat("MMMM dd, yyyy");
        Date date = new Date(currentTime);

        return simpleFormatDate.format(date);

    }

    public static String getTimeF(Long currentTime){

        //Time Calculation
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleFormatTime = new SimpleDateFormat("hh:mm:ss:a");
        Date date = new Date(currentTime);

        return simpleFormatTime.format(date);

    }
}
