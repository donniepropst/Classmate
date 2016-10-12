package com.tributetech.classmate.domain;

import com.tributetech.classmate.utils.Time;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Donnie Propst on 2/15/2016.
 */
public class Class {

    private int ID; //from database
    private String className;
    private String room; //optional
    private String days; // 0 - 7;
    private Time startTime;
    private Time endTime;

    private String color;
    private int semesterID;

    public Class(String className, String room, String days, Time startTime, Time endTime, String color, int semesterID) {
        this.className = className;
        this.room = room;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
        this.semesterID = semesterID;
    }

    public Class(){

    }

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getDays() {
        return days;
    }
    public void setDays(String days) {
        this.days = days;
    }

    //START TIME
    public Time getStartTime() {
        return startTime;
    }
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    //END TIME
    public Time getEndTime() {
        return endTime;
    }
    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public int getSemesterID(){return semesterID;}
    public void setSemesterID(int s){semesterID = s;}



}
