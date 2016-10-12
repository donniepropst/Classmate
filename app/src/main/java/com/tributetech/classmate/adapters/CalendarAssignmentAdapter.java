package com.tributetech.classmate.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tributetech.classmate.R;
import com.tributetech.classmate.domain.Assignment;
import com.tributetech.classmate.domain.Class;
import com.tributetech.classmate.utils.CalendarUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Donnie Propst on 5/23/2016.
 */
public class CalendarAssignmentAdapter extends RecyclerView.Adapter<CalendarAssignmentAdapter.AssignmentViewHolder> {

    public ArrayList<Assignment> assignmentArrayList;
    private HashMap<Integer, Class> classes;

    public CalendarAssignmentAdapter(HashMap<Integer, Class> classes){
        this.classes = classes;
        assignmentArrayList = new ArrayList<>();

    }
    public void updateData(ArrayList<Assignment> list){
        assignmentArrayList = list;
        notifyDataSetChanged();
    }

    @Override
    public CalendarAssignmentAdapter.AssignmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_card, parent, false);
        AssignmentViewHolder vh = new AssignmentViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(AssignmentViewHolder holder, int position) {
        holder.assignmentName.setText(assignmentArrayList.get(position).getAssignmentName() + " - ");
        holder.className.setText(classes.get(assignmentArrayList.get(position).getClassID()).getClassName());
        holder.dueDate.setText(CalendarUtils.getSlashDate(assignmentArrayList.get(position).getDueDate()));
        holder.dueTime.setText(assignmentArrayList.get(position).getDueTime().toString());
        holder.itemView.setPadding(0,0,0,0);
        if(position == assignmentArrayList.size()-1){
            holder.itemView.setPadding(0,0,0,400);
        }
    }

    @Override
    public int getItemCount() {
        return assignmentArrayList.size();
    }

    public static class AssignmentViewHolder extends RecyclerView.ViewHolder{
        public TextView assignmentName;
        public TextView className;
        public TextView dueTime;
        public TextView dueDate;

        public AssignmentViewHolder(View itemView) {
            super(itemView);
            assignmentName = (TextView)itemView.findViewById(R.id.calendar_item_assignment_name);
            className = (TextView)itemView.findViewById(R.id.calendar_item_class_name);
            dueTime = (TextView)itemView.findViewById(R.id.calendar_item_due_time);
            dueDate = (TextView)itemView.findViewById(R.id.calendar_item_due_date);
        }
    }
}
