package com.tributetech.classmate.utils;

import android.util.Log;

import java.text.CharacterIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Donnie Propst on 2/15/2016.
 */
public class CalendarUtils {
    public static ArrayList<String> dayList = new ArrayList<>();


    public static String getFormattedDate(){

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        return formatDate(c.getTime());
    }
    public static String getFormattedDate(int year, int m, int date){

        Calendar c = Calendar.getInstance();
        c.set(year, m, date);
        return formatDate(c.getTime());
    }

    public static String getFormattedTime(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        return formatTime(c.getTime());
    }
    public static String getFormattedTime(int hour, int min){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        return formatTime(c.getTime());
    }
    public static Date getDateTimeObject(int hour, int min){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        System.out.println("CALENDAR TIME: " + c.getTime());
        return c.getTime();
    }

    public static String getFormattedDate(Date d){
        return formatDate(d);
    }

    private static String formatDate(Date d){
        SimpleDateFormat day = new SimpleDateFormat("EE, MMM d, yyyy");
        return day.format(d);
    }
    private static String formatTime(Date d){
        SimpleDateFormat time = new SimpleDateFormat("h:mm a");
        return time.format(d);
    }



    public static String getSlashDate(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        return sdf.format(d);
    }

    public static String formatDay(String dayWord){
        if(dayList.size() == 0)
            setupDayList();

        StringBuilder blockDayText = new StringBuilder();
        for(char c : dayWord.toCharArray()){
           blockDayText.append((blockDayText.toString().trim().isEmpty() ? "" : ", ") + dayList.get(Character.getNumericValue(c)).substring(0,3));
        }
        return blockDayText.toString();

    }

    private static void setupDayList(){
        dayList.add("Monday");
        dayList.add("Tuesday");
        dayList.add("Wednesday");
        dayList.add("Thursday");
        dayList.add("Friday");
        dayList.add("Saturday");
        dayList.add("Sunday");
    }




}
