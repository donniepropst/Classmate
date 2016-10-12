package com.tributetech.classmate.customui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.tributetech.classmate.activities.AddSemester;
import com.tributetech.classmate.domain.Semester;

import java.util.ArrayList;

/**
 * Created by Donnie Propst on 5/20/2016.
 */
public class SemesterSpinner extends AppCompatSpinner {

    private ArrayList<Semester> semesters;
    private ArrayAdapter<String> semesterNamesAdapter;


    public SemesterSpinner(Context context) {
        super(context);
    }
    public SemesterSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SemesterSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public SemesterSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }
    public SemesterSpinner(Context context, int mode) {
        super(context, mode);
    }

    public void init(ArrayList<Semester> semesters){
        this.semesters = semesters;
        ArrayList<String> semesterNameList = new ArrayList<String>();
        for(Semester s : semesters){
            semesterNameList.add(s.getName());
        }
        semesterNameList.add("Add a Semester");

        semesterNamesAdapter = new ArrayAdapter<String>(super.getContext(), android.R.layout.simple_spinner_item, semesterNameList);
        semesterNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.setAdapter(semesterNamesAdapter);

        setupListeners();
    }

    private void setupListeners(){
        setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position == semesters.size()){
                    getContext().startActivity(new Intent(parentView.getContext(), AddSemester.class));
                    setSelection(getSelectedItemPosition() - 1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    public Semester getSelectedSemester(){
        return semesters.get(getSelectedItemPosition());
    }

    public void setPreselected(int semesterID){
        search:
        for(int i = 0; i < semesters.size(); i++){
            if(semesters.get(i).getID() == semesterID){
                setSelection(i);
                break search;
            }
        }

    }



}
