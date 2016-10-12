package com.tributetech.classmate.utils;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Donnie Propst on 2/18/2016.
 */
public class Time {

    private int hour;
    private int minute;
    private boolean isAM; // true = AM, false = PM
    //hh:mm AM
    //01234567
    public Time(int hour, int minute, boolean isAM){
        this.hour = hour;
        this.minute = minute;
        this.isAM = isAM;
    }

    public Time(Date d){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        minute = c.get(Calendar.MINUTE);
        hour = c.get(Calendar.HOUR);
        if(hour == 0)
            hour = 12;
        isAM = c.get(Calendar.AM_PM) == Calendar.AM;
    }

    public static Time getNow(){
        Calendar c = Calendar.getInstance();
        boolean amPM = false;
        if(c.get(Calendar.AM_PM) == Calendar.AM){
            amPM = true;
        }
        int h = c.get(Calendar.HOUR);
        if(h == 0)
            h = 12;
        return new Time(h, c.get(Calendar.MINUTE),amPM);
    }
    public static Time getHourAdvanced(){
        Calendar c = Calendar.getInstance();
        final long oneHourMillis = 3600000;
        boolean amPM = false;

        c.setTimeInMillis(System.currentTimeMillis()+oneHourMillis);
        if(c.get(Calendar.AM_PM) == Calendar.AM){
            amPM = true;
        }
        int h = c.get(Calendar.HOUR);
        if(h == 0)
            h = 12;
        return new Time(h, c.get(Calendar.MINUTE),amPM);

    }

    public Time(String time){
          isAM =  time.contains("A");
          int colonIndex = time.indexOf(':');
          String minuteString = time.charAt(colonIndex+1) + ""+ time.charAt(colonIndex+2);
          minute = Integer.valueOf(minuteString.trim());
          String hourString ="";
          for(char c : time.toCharArray()){
              if(c==':'){
                  break;
              }else{
                  hourString+=c;
              }
          }
        hour = Integer.valueOf(hourString);
      }


    public String convertToString(){
        DecimalFormat dfTwo = new DecimalFormat("00");
        return hour+":"+dfTwo.format(minute)+ " " + (isAM ? "AM" : "PM");
    }

    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }
    public boolean isAM() {
        return isAM;
    }
    public void setIsAM(boolean isAM) {
        this.isAM = isAM;
    }

    @Override
    public String toString() {
        return convertToString();
    }
}
