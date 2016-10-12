package com.tributetech.classmate.domain;

import java.util.Date;

/**
 * Created by Donnie Propst on 3/5/2016.
 */
public class Semester {
    private Date startDate;
    private Date endDate;
    private String name;
    private int ID;

    public Semester(Date sD, Date eD, String name){
        startDate = sD;
        endDate = eD;
        this.name = name;
    }
    public Semester(Date sD, Date eD, String name, int ID){
        startDate = sD;
        endDate = eD;
        this.name = name;
        this.ID = ID;
    }
    public Semester(){}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    //START DATE//
    public Date getStartDate() {
        return startDate;
    }
    public long getStartDateMilliseconds(){
        return startDate.getTime();
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public void setStartDate(long millis){
        if(startDate == null)
            startDate = new Date();
        startDate.setTime(millis);
    }

    //END DATE//
    public Date getEndDate() {
        return endDate;
    }
    public long getEndDateMilliseconds() {
        System.out.println("HEREHREURHEIUHRIUEHRIUSERSEHURI");
        System.out.println("MILLIS: "+ endDate.getTime());
        return endDate.getTime();
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public void setEndDate(long millis) {
        if(endDate == null)
            endDate = new Date();
        endDate.setTime(millis);
    }

    public boolean areDatesSet(){
        return startDate != null && endDate != null;
    }

    public void print(){
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);
        System.out.println("Name: " + name);

    }

}
