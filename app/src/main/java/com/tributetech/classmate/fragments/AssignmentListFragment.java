package com.tributetech.classmate.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tributetech.classmate.R;
import com.tributetech.classmate.activities.HomeActivity;
import com.tributetech.classmate.adapters.AssignmentAdapter;
import com.tributetech.classmate.adapters.ClassAdapter;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.domain.Assignment;
import com.tributetech.classmate.domain.Class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Adam on 2/29/2016.
 */
public class AssignmentListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private AssignmentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView emptyText;


    public AssignmentListFragment() {
        // Required empty public constructor
    }



    public static AssignmentListFragment newInstance() {
        AssignmentListFragment fragment = new AssignmentListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assignment_list, container, false);
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHelper db = new DatabaseHelper(view.getContext());
        if(!db.getAllAssignments().isEmpty()) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.assignment_recyclerlist);

            mLayoutManager = new LinearLayoutManager(view.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);


            HashMap<Integer, Class> classHashMap = new HashMap<>();
            for (Class c : db.getAllClasses()) {
                classHashMap.put(c.getID(), c);
            }

            mAdapter = new AssignmentAdapter(db.getAllAssignments(), classHashMap);
            mRecyclerView.setAdapter(mAdapter);
            if (mAdapter.getNearestDateHeader() > 0) {
                mRecyclerView.scrollToPosition(mAdapter.getNearestDateHeader());
            }
        }else{
            emptyText = (TextView)view.findViewById(R.id.fragment_assignment_empty_text);
            emptyText.setText("You haven't added any assignments yet.\n Press the + button to get started.");
            emptyText.setVisibility(View.VISIBLE);
        }

    }

    public void refine(int classID){
        DatabaseHelper db = new DatabaseHelper(this.getContext());
        HashMap<Integer, Class> classHashMap = new HashMap<>();
        for(Class c : db.getAllClasses()){
            classHashMap.put(c.getID(), c);
        }
        ArrayList<Assignment> assignments = db.getAllAssignments();
        Iterator<Assignment> iter = assignments.iterator();
        while(iter.hasNext()){
            Assignment a = iter.next();
            if(a.getClassID() != classID)
                iter.remove();
        }

        mAdapter = new AssignmentAdapter(assignments, classHashMap);
        mRecyclerView.setAdapter(mAdapter);
    }




}
