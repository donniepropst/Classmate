package com.tributetech.classmate.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tributetech.classmate.R;
import com.tributetech.classmate.domain.Assignment;
import com.tributetech.classmate.domain.Class;
import com.tributetech.classmate.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by Donnie Propst on 5/28/2016.
 */
public class ClassAssignmentAdapter  extends RecyclerView.Adapter<ClassAssignmentAdapter.ViewHolder>{

    private ArrayList<Assignment> assignments;
    private Class forClass;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView assignmentName;
        public TextView assignmentDate;
        public TextView assignmentTime;
        public ImageView doneStatus;
        public ViewHolder(View v) {
            super(v);
            assignmentName = (TextView)v.findViewById(R.id.dialog_assignment_name);
            assignmentDate = (TextView)v.findViewById(R.id.dialog_assignment_date);
            assignmentTime = (TextView)v.findViewById(R.id.dialog_assignment_time);
            doneStatus = (ImageView)v.findViewById(R.id.dialog_assignment_check);
        }
    }

    public ClassAssignmentAdapter(ArrayList<Assignment> assignments, Class forClass) {
        this.assignments = assignments;
        this.forClass = forClass;
        Iterator<Assignment> iter = assignments.iterator();
        while(iter.hasNext()){
            if(iter.next().getClassID() != forClass.getID()){
                iter.remove();
            }
        }
        Collections.sort(assignments, new Comparator<Assignment>() {
            @Override
            public int compare(Assignment assignment, Assignment t1) {
                if(assignment.getDueDate().before(t1.getDueDate()))
                    return -1;
                return 1;
            }
        });
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ClassAssignmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basic_assignment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.assignmentName.setText(assignments.get(position).getAssignmentName());
        holder.assignmentDate.setText(CalendarUtils.getSlashDate(assignments.get(position).getDueDate()));
        holder.assignmentTime.setText(assignments.get(position).getDueTime().convertToString());

        if(assignments.get(position).getComplete()){
            holder.doneStatus.setVisibility(View.VISIBLE);
            holder.doneStatus.setColorFilter(Color.parseColor(forClass.getColor()));
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return assignments.size();
    }


}
