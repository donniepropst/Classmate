package com.tributetech.classmate.domain;

import android.graphics.Color;

import com.tributetech.classmate.utils.Time;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Donnie Propst on 2/18/2016.
 */
public class ClassBuilder {

    private int ID; //from database
    private String className = "";
    private String room = ""; //optional
    private String days = "0"; // 0 - 6;
    private ArrayList<Integer> daysSelected;
    private Time startTime;
    private Time endTime;
    private String color;

    private int semesterID;


    public ClassBuilder(){
        daysSelected = new ArrayList<>();
    }

    public ClassBuilder(Class createdClass){
        className = createdClass.getClassName();
        room = createdClass.getRoom();
        days = createdClass.getDays();
        startTime = createdClass.getStartTime();
        endTime = createdClass.getEndTime();
        color = createdClass.getColor();
        semesterID = createdClass.getSemesterID();
    }

    public void clearDays(){
        daysSelected = new ArrayList<>();
    }
    public void addClassName(String c){
        className = c;
    }

    public void addClassRoom(String r){
        room = r;
    }
    public void addClassDays(String d){
        days = d;
    }

    public void addStartTime(Date d){
        startTime = new Time(d);
    }
    public void addEndTime(Date d){
        endTime = new Time(d);
    }


    public void addColor(String c){color = c;}

    public void setDays(boolean[] selected){
        StringBuilder dayWord = new StringBuilder();
        clearDays();
        for(int i = 0; i < selected.length; i++){
            if(selected[i]) {
                daysSelected.add(i);
                dayWord.append(i);
            }
        }
        days = dayWord.toString();
    }

    public void setSemesterID(int s){semesterID = s;}

    public Class build(){
        return new Class(className, room, days, startTime, endTime, color, semesterID);
    }

    public String getColor(){return color;}


}
