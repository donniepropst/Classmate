package com.tributetech.classmate.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import com.tributetech.classmate.R;
import com.tributetech.classmate.adapters.ClassAdapter;
import com.tributetech.classmate.adapters.ClassAssignmentAdapter;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.domain.Class;

/**
 * Created by Donnie Propst on 5/28/2016.
 */
public class ClassAssignmentDialog extends DialogFragment {

    private RecyclerView assignmentRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Class classForAssignment;

    public ClassAssignmentDialog(){

    }

    public static ClassAssignmentDialog newInstance() {
        ClassAssignmentDialog frag = new ClassAssignmentDialog();
        return frag;
    }

    public void setClass(Class classForAssignment){
        this.classForAssignment = classForAssignment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(classForAssignment.getClassName() + " Assignments" );
        builder.setNegativeButton("Close", null);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_assignment_list, null);

        builder.setView(view);

        DatabaseHelper db = new DatabaseHelper(view.getContext());
        assignmentRecyclerView = (RecyclerView) view.findViewById(R.id.class_assignment_recyclerview);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(view.getContext());
        assignmentRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ClassAssignmentAdapter(db.getAllAssignments(), classForAssignment);
        assignmentRecyclerView.setAdapter(mAdapter);

        return builder.create();
    }
}
