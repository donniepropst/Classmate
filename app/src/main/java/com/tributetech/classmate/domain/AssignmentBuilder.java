package com.tributetech.classmate.domain;

import com.tributetech.classmate.utils.Time;

import java.util.Date;

/**
 * Created by Adam on 2/28/2016.
 */
public class AssignmentBuilder {

        private int ID; //from database
        private String assignmentName = "";
        private int classID;
        private Time dueTime;
        private Date dueDate;
        private String description;
        private Boolean remind;
        private Boolean complete;

        public AssignmentBuilder(){

        }

        public AssignmentBuilder(Assignment a){
            ID = a.getID();
            assignmentName = a.getAssignmentName();
            classID = a.getClassID();
            dueTime = a.getDueTime();
            dueDate = a.getDueDate();
            description = a.getDescription();
            remind = a.getRemind();
            complete = a.getComplete();
        }

        public void addID(int i) {
            ID = i;
        }

        public void addAssignmentName(String a){
            assignmentName = a;
        }
        public void addClassID(int c){
            classID = c;
        }
        public void addDueTime(String s){
            dueTime = new Time(s);
        }
        public void addDueDateMilli(long t){
            dueDate = new Date();
            dueDate.setTime(t);
        }
        public void addDueDate(Date d) {
            dueDate = d;
        }
        public void addDescription(String d) {
            description = d;
        }
        public void addReminder(int r) {
            remind = r == 1;
        }
        public void addComplete(int c) {
            complete = c == 1;
        }

        public void addReminder(boolean b){
            remind = b;
        }
        public void addComplete(boolean b){
            complete = b;
        }

        public Assignment build(){
            return new Assignment(ID, classID, assignmentName, dueDate, dueTime, description, remind, complete);
        }


}
