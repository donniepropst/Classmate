package com.tributetech.classmate.domain;

import com.tributetech.classmate.utils.Time;

import java.util.Date;

/**
 * Created by Donnie Propst on 2/15/2016.
 */
public class Assignment {

    private int ID;
    private int classID;
    private String assignmentName;
    private Date dueDate;
    private Time dueTime;
    private String description;
    private Boolean remind;
    private Boolean complete;

    public Assignment(int ID, int classID, String assignmentName, Date dueDate, Time dueTime, String description, Boolean remind, Boolean complete) {
        this.ID = ID;
        this.classID = classID;
        this.assignmentName = assignmentName;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.description = description;
        this.remind = remind;
        this.complete = complete;
    }


    public int getID() {
        return ID;
    }

    public int getClassID() {
        return classID;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Time getDueTime() {
        return dueTime;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getRemind() {
        return remind;
    }

    public Boolean getComplete(){return complete;}
    public void setComplete(boolean b){
        complete = b;
    }
    public long convertDateToMillis(){
        return dueDate.getTime();
    }
}
