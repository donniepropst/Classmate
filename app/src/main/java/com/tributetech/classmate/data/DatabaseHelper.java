package com.tributetech.classmate.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tributetech.classmate.domain.Assignment;
import com.tributetech.classmate.domain.AssignmentBuilder;
import com.tributetech.classmate.domain.Class;
import com.tributetech.classmate.domain.Semester;
import com.tributetech.classmate.utils.Time;

import java.util.ArrayList;

/**
 * Created by Donnie Propst on 2/16/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //CLASS STUFF
    public static final String DATABASE_NAME = "class.db";
    public static final String CLASS_TABLE = "class_table";
    public static final String ID_COL1 = "ID";
    public static final String NAME_COL2 = "NAME";
    public static final String ROOM_COL3 = "ROOM";
    public static final String DAYS_COL4 = "DAYS";
    public static final String START_TIME_COL5 = "START_TIME";
    public static final String END_TIME_COL6 = "END_TIME";
    public static final String COLOR_COL7 = "COLOR";
    //public static final String CLASS_SEMESTER_ID_RELATION_COL8 = "CLASS_SEMESTER_ID";


    public static final int ID = 0;
    public static final int NAME=1;
    public static final int ROOM=2;
    public static final int DAYS=3;
    public static final int START_TIME=4;
    public static final int END_TIME=5;
    public static final int COLOR=6;
    //public static final int CLASS_SEMESTER = 7;

    //ASSIGNMENT STUFF
    public static final String ASSIGNMENT_TABLE = "assignment_table";
    public static final String ASSIGNMENT_ID_COL1 = "ID";
    public static final String CLASSID_COL1 = "CLASS_ID";
    public static final String ASSIGNMENT_NAME_COL2 = "ASSIGNMENT_NAME";
    public static final String DUE_DATE_COL3 = "DUE_DATE";
    public static final String DUE_TIME_COL4 = "DUE_TIME";
    public static final String DESCRIPTION_COL5 = "DESCRIPTION";
    public static final String REMIND_COL6 = "REMIND";
    public static final String COMPLETE_COL7 = "COMPLETE";

    public static final int ASSIGNMENT_ID = 0;
    public static final int CLASS_ID = 1;
    public static final int ASSIGNMENT_NAME = 2;
    public static final int DUE_DATE = 3;
    public static final int DUE_TIME = 4;
    public static final int DESCRIPTION = 5;
    public static final int REMIND = 6;
    public static final int COMPLETE = 7;

    /*
    public static final String SEMESTER_TABLE="semester_table";
    public static final String SEMESTER_ID_COL1 = "ID";
    public static final String SEMESTER_START_DATE_COL2 = "START_DATE";
    public static final String SEMESTER_END_DATE_COL3 = "END_DATE";
    public static final String SEMESTER_NAME_COL4 = "NAME";

    public static final int SEMESTER_ID = 0;
    public static final int S_START_DATE = 1;
    public static final int S_END_DATE = 2;
    public static final int S_NAME = 3;
    */

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CLASS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, " +
                "ROOM TEXT, DAYS TEXT, START_TIME TEXT, END_TIME TEXT, COLOR TEXT)");
        sqLiteDatabase.execSQL("create table " + ASSIGNMENT_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, CLASS_ID INT, " +
                "ASSIGNMENT_NAME TEXT, DUE_DATE LONG, DUE_TIME TEXT, DESCRIPTION TEXT, REMIND INT, COMPLETE INT)");
        //sqLiteDatabase.execSQL("create table " + SEMESTER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, START_DATE LONG, " +
        //"END_DATE LONG, NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ASSIGNMENT_TABLE);
       // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SEMESTER_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean insertClass(Class classObject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
            contentValues.put(NAME_COL2, classObject.getClassName());
            contentValues.put(ROOM_COL3, classObject.getRoom());
            contentValues.put(DAYS_COL4, classObject.getDays());
            contentValues.put(START_TIME_COL5, classObject.getStartTime().convertToString());
            contentValues.put(END_TIME_COL6, classObject.getEndTime().convertToString());
            contentValues.put(COLOR_COL7, classObject.getColor());
           // contentValues.put(CLASS_SEMESTER_ID_RELATION_COL8, classObject.getSemesterID());
            long result = db.insert(CLASS_TABLE, null, contentValues);
        if(result == -1){
            Log.d("Database Error", "ERROR INSERTING");
        }else{
            Log.d("Database Success", "INSERTED CLASS");
            return true;
        }
        db.close();
        return false;
    }
    public boolean insertAssignment(Assignment assignmentObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
            contentValues.put(CLASSID_COL1, assignmentObject.getClassID());
            contentValues.put(ASSIGNMENT_NAME_COL2, assignmentObject.getAssignmentName());
            contentValues.put(DUE_DATE_COL3, assignmentObject.getDueDate().getTime());
            contentValues.put(DUE_TIME_COL4, assignmentObject.getDueTime().convertToString());
            contentValues.put(DESCRIPTION_COL5, assignmentObject.getDescription());
            contentValues.put(REMIND_COL6, assignmentObject.getRemind());
            contentValues.put(COMPLETE_COL7, assignmentObject.getComplete());

        long result = db.insert(ASSIGNMENT_TABLE, null, contentValues);

        if(result == -1) {
            Log.d("Database Error", "ERROR INSERTING");
        } else {
            Log.d("Database Success", "INSERTED ASSIGNMENT");
            return true;
        }
        db.close();
        return false;
    }
   /* public void insertSemester(Semester sObject, DatabaseUpdateCallback updateCallback){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
            contentValues.put(SEMESTER_START_DATE_COL2, sObject.getStartDateMilliseconds());
            contentValues.put(SEMESTER_END_DATE_COL3, sObject.getEndDateMilliseconds());
            contentValues.put(SEMESTER_NAME_COL4, sObject.getName());
            long result = db.insert(SEMESTER_TABLE, null, contentValues);
        if(result == -1) {
            Log.d("Database Error", "ERROR INSERTING SEMESTTER");
        } else {
            Log.d("Database Success", "INSERTED SEMESTER");
        }
        db.close();
        updateCallback.finished();
    }*/


    public void updateClass(Class classObject){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
            cv.put(NAME_COL2, classObject.getClassName());
            cv.put(ROOM_COL3, classObject.getRoom());
            cv.put(DAYS_COL4, classObject.getDays());
            cv.put(START_TIME_COL5, classObject.getStartTime().convertToString());
            cv.put(END_TIME_COL6, classObject.getEndTime().convertToString());
            cv.put(COLOR_COL7, classObject.getColor());
           // cv.put(CLASS_SEMESTER_ID_RELATION_COL8, classObject.getSemesterID());

        long result = db.update(CLASS_TABLE, cv, ID_COL1 + "="+classObject.getID(), null);
        db.close();
    }

    public void updateAssignment(Assignment assignmentObject){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
                contentValues.put(CLASSID_COL1, assignmentObject.getClassID());
                contentValues.put(ASSIGNMENT_NAME_COL2, assignmentObject.getAssignmentName());
                contentValues.put(DUE_DATE_COL3, assignmentObject.getDueDate().getTime());
                contentValues.put(DUE_TIME_COL4, assignmentObject.getDueTime().convertToString());
                contentValues.put(DESCRIPTION_COL5, assignmentObject.getDescription());
                contentValues.put(REMIND_COL6, assignmentObject.getRemind());
                contentValues.put(COMPLETE_COL7, assignmentObject.getComplete());

            long result = db.update(ASSIGNMENT_TABLE, contentValues,ASSIGNMENT_ID_COL1+"="+assignmentObject.getID(),null);
            db.close();
    }


    public ArrayList<Assignment> getAllAssignments() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Assignment> resultAssignments = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM " + ASSIGNMENT_TABLE, null);
        if(res.getCount() == 0) {
            //no assignments
        } else {
            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()) {

                int id = res.getInt(ASSIGNMENT_ID);
                int classID = res.getInt(CLASS_ID);
                String name = res.getString(ASSIGNMENT_NAME);
                long dueDate = res.getLong(DUE_DATE);
                String dueTime = res.getString(DUE_TIME);
                String description = res.getString(DESCRIPTION);
                int remind = res.getInt(REMIND);
                int complete = res.getInt(COMPLETE);

                AssignmentBuilder ab = new AssignmentBuilder();
                ab.addID(id);
                ab.addClassID(classID);
                ab.addAssignmentName(name);
                ab.addDueDateMilli(dueDate);
                ab.addDueTime(dueTime);
                ab.addDescription(description);
                ab.addReminder(remind);
                ab.addComplete(complete);

                Assignment a = ab.build();
                resultAssignments.add(a);
            }
        }
        res.close();
        db.close();
        return resultAssignments;
    }
    public ArrayList<Class> getAllClasses(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Class> resultClasses = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM " + CLASS_TABLE, null);
        if(res.getCount() == 0){
            //no classes yet
        }else{
            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()){

                int id = res.getInt(ID);
                System.out.println("CURRENT ID IS: " + id);
                String name = res.getString(NAME);
                String room = res.getString(ROOM);
                String days = res.getString(DAYS);
                String start_time = res.getString(START_TIME);
                String end_time = res.getString(END_TIME);
                String color = res.getString(COLOR);
                //int semester = res.getInt(CLASS_SEMESTER);

                Class c = new Class();
                c.setID(id);
                c.setClassName(name);
                c.setRoom(room);
                c.setDays(days);
                c.setStartTime(new Time(start_time));
                c.setEndTime(new Time(end_time));
                c.setColor(color);
                //c.setSemesterID(semester);

                resultClasses.add(c);
            }
        }
        res.close();
        db.close();
        return resultClasses;
    }
    /*public ArrayList<Semester> getAllSemesters(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Semester> resultSemester = new ArrayList<>();
        Cursor res = db.rawQuery("SELECT * FROM " + SEMESTER_TABLE, null);
        if(res.getCount() == 0){
        }else{
            while(res.moveToNext()){
                Semester s = new Semester();
                s.setID(res.getInt(SEMESTER_ID));
                s.setStartDate(res.getLong(S_START_DATE));
                s.setEndDate(res.getLong(S_END_DATE));
                s.setName(res.getString(S_NAME));
                resultSemester.add(s);
            }
        }
        res.close();
        db.close();
        return resultSemester;
    }*/
    public void deleteAssignment(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ASSIGNMENT_TABLE + " where " + ASSIGNMENT_ID_COL1 + "='" + ID + "'");
        db.close();
    }
    public void deleteClass(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ CLASS_TABLE + " where "+ID_COL1+"='"+ID+"'");
        db.execSQL("delete from "+ASSIGNMENT_TABLE + " where " + CLASSID_COL1 + "='" + ID + "'");
        System.out.println("DELETED CLASS");

        db.close();
       // db.execSQL("delete from "+TBL_NAME+" where Google='"+id+"'");
    }



}
