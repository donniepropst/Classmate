package com.tributetech.classmate.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.tributetech.classmate.R;
import com.tributetech.classmate.adapters.CalendarAssignmentAdapter;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.domain.Assignment;
import com.tributetech.classmate.domain.Class;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CalendarFragment extends Fragment {

    private CompactCalendarView calendarView;
    private ImageView next;
    private ImageView prev;
    private TextView month;

    private DatabaseHelper db;
    private ArrayList<Class> classes;
    private HashMap<Integer, Class> classHashMap;

    private RecyclerView assignmentList;
    private CalendarAssignmentAdapter assignmentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    public CalendarFragment() {
    }


    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(view.getContext());

        calendarView = (CompactCalendarView) view.findViewById(R.id.calendar_fragment_calendar);
        calendarView.shouldScrollMonth(false);
        next = (ImageView)view.findViewById(R.id.next);
        prev = (ImageView)view.findViewById(R.id.prev);
        month = (TextView)view.findViewById(R.id.month);

        populateCalendar();
        updateMonthText();
        setupButtons();
        setupRecyclerView(view);
        setupCalendarListener();
    }

    private void updateMonthText(){
        month.setText(dateFormatForMonth.format(calendarView.getFirstDayOfCurrentMonth()));
    }

    private void populateCalendar(){
         classes = db.getAllClasses();
         classHashMap = new HashMap<>();

        for(Class c : classes){
            classHashMap.put(c.getID(), c);
        }
        ArrayList<Event> events = new ArrayList<>();
        for (Assignment a : db.getAllAssignments()) {
            try {
                int color = Color.parseColor(classHashMap.get(a.getClassID()).getColor());
                events.add(new Event(color, a.getDueDate().getTime(), a));
            }catch(NullPointerException e){

            }
        }
        calendarView.addEvents(events);
    }

    private void setupButtons(){

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                calendarView.showNextMonth();
                updateMonthText();
            }
        });
        prev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                calendarView.showPreviousMonth();
                updateMonthText();
            }
        });
    }

    private void setupRecyclerView(View v){
        assignmentList = (RecyclerView) v.findViewById(R.id.calendar_fragment_assignment_recyclerview);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(v.getContext());
        assignmentList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        assignmentAdapter = new CalendarAssignmentAdapter(classHashMap);
        assignmentList.setAdapter(assignmentAdapter);
    }

    private void setupCalendarListener(){
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
               setupAdapter(dateClicked);
            }
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });

        setupAdapter(Calendar.getInstance().getTime());


    }

    private void setupAdapter(Date d){
        ArrayList<Assignment> assignmentForDate = new ArrayList<Assignment>();
        List<Event> events = calendarView.getEvents(d);
        for(Event e : events){
            assignmentForDate.add((Assignment)e.getData());
            System.out.println("FOUND 1");
        }
        assignmentAdapter.updateData(assignmentForDate);
    }


}
