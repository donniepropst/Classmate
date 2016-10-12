package com.tributetech.classmate.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tributetech.classmate.R;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.domain.Assignment;
import com.tributetech.classmate.domain.Class;
import com.tributetech.classmate.fragments.AssignmentListFragment;
import com.tributetech.classmate.utils.BottomSheet;
import com.tributetech.classmate.utils.CalendarUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AssignmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ModifiableAdapter {


    private ArrayList<Object> dataSet;

    private ArrayList<Assignment> assignments;
    private HashMap<Integer, Class> classHashMap;


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_EMPTY = 2;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AssignmentAdapter(ArrayList<Assignment> assignments, HashMap<Integer, Class> classHashMap) {
        BottomSheet.assignmentAdapterReference = this;

        dataSet = new ArrayList<>();
        this.classHashMap = classHashMap;
        this.assignments = assignments;

        generateDataSet();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment_header, parent, false);
            return new AssignmentDateHolder(v);
        }else if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment_card, parent, false);
            return new AssignmentHolder(v);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);
            return new EmptyPadding(v);
        }


    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof AssignmentDateHolder)
        {
            AssignmentHeader header = (AssignmentHeader)dataSet.get(position);
            ((AssignmentDateHolder) holder).dateTextView.setText(header.getDateString());
        }
        else if(holder instanceof AssignmentHolder)
        {
            final Assignment item = (Assignment)dataSet.get(position);
            ((AssignmentHolder) holder).className.setText(classHashMap.get(item.getClassID()).getClassName() + " - " );
            ((AssignmentHolder) holder).assignmentName.setText(item.getAssignmentName());

            ((AssignmentHolder) holder).dueTime.setText(item.getDueTime().convertToString());
            ((AssignmentHolder) holder).checkBox.setChecked(item.getComplete());
            ((AssignmentHolder) holder).classID = item.getClassID();
            ((AssignmentHolder)holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    item.setComplete(compoundButton.isChecked());
                    new AsyncSave(compoundButton.getContext()).execute(item);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    BottomSheet bs = new BottomSheet(holder.itemView, ((Assignment)dataSet.get(position)).getID(), position, dataSet.get(position));
                    bs.createBottomDialog();
                    return true;
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
      if(dataSet.get(position) instanceof AssignmentHeader){
          return TYPE_HEADER;
      }else if(dataSet.get(position) instanceof Assignment)
        return TYPE_ITEM;

        return TYPE_EMPTY;
    }


    @Override
    public void remove(int index) {
        dataSet.remove(index);

        //if(index+1 <= dataSet.size()-1) {
        //    if (index + 1 <= dataSet.size() - 1 || !(dataSet.get(index + 1) instanceof Assignment) || dataSet.get(index+1) instanceof AssignmentHeader)
        //        dataSet.remove(index+);
        //}
        //if(dataSet.get(index-1) != null && dataSet.get(index-1) instanceof AssignmentHeader && (dataSet.size() > index) && (dataSet.get(index+1) == null || dataSet.get(index+1) instanceof AssignmentHeader))
         //   dataSet.remove(index-1);
        if(dataSet.size() == 1){ //edge
            dataSet.remove(0);
        }

        notifyDataSetChanged();
    }


    public static class AssignmentHolder extends RecyclerView.ViewHolder {
        public TextView assignmentName;
        public TextView className;
        public CheckBox checkBox;
        public TextView dueTime;
        public int classID;
        public AssignmentHolder(View v) {
            super(v);
            assignmentName = (TextView)v.findViewById(R.id.assignment_header_name_text);
            className = (TextView)v.findViewById(R.id.assignment_header_class_text);
            checkBox = (CheckBox)v.findViewById(R.id.assignment_header_complete_checkbox);
            dueTime = (TextView)v.findViewById(R.id.assignment_header_duetime);
        }
    }

    public static class AssignmentDateHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;

        public AssignmentDateHolder(View itemView) {
            super(itemView);
            dateTextView = (TextView)itemView.findViewById(R.id.assignment_header_date_text);
        }
    }

    public static class EmptyPadding extends RecyclerView.ViewHolder{
        public EmptyPadding(View itemView) {
            super(itemView);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private class AsyncSave extends AsyncTask<Assignment, Void, Void> {
        private DatabaseHelper db;

        public AsyncSave(Context c){
            db = new DatabaseHelper(c);

        }

        @Override
        protected Void doInBackground(Assignment... assignments) {
            db.updateAssignment(assignments[0]);
            return null;
        }
    }

    public int getNearestDateHeader(){
        final long now = System.currentTimeMillis();

        HashMap<Integer, Date> datePositionMap = new HashMap<>();
        for(int i = 0; i < dataSet.size(); i++){
            if(dataSet.get(i) instanceof AssignmentHeader){
                datePositionMap.put(i,(((AssignmentHeader) dataSet.get(i)).getDate()));
            }
        }
        Date nowDate = Calendar.getInstance().getTime();
        int lastPosition = 0;
        for (Map.Entry<Integer, Date> entry : datePositionMap.entrySet()) {
            //int key = entry.getKey();
            Date date = entry.getValue();
            lastPosition = entry.getKey();
            if(date.after(nowDate)){
                return entry.getKey();
            }
        }
        return lastPosition; //all assignments are older than today, so scroll to the end of the rv

    }

    private void generateDataSet(){
        Collections.sort(assignments, new Comparator<Assignment>() {
            @Override
            public int compare(Assignment assignment, Assignment t1) {
                if(assignment.getDueDate().before(t1.getDueDate()))
                    return -1;
                return 1;
            }
        });

        for(Assignment a : assignments){
            AssignmentHeader h = new AssignmentHeader(a.getDueDate());
            if(!dataSet.contains(h)){
                dataSet.add(h);
            }
            dataSet.add(a);
        }
        dataSet.add(new EmptyView());
    }

    public void removeClass(int deletedClassID){
        //classHashMap.remove(deletedClassID);

      /* for(int i = 0; i < dataSet.size(); i++){
           if(dataSet.get(i) instanceof Assignment){
                if(((Assignment) dataSet.get(i)).getClassID() == deletedClassID){
                    remove(i);
                }
           }
       }*/
        for(int i = 0; i < assignments.size(); i++){
            if(assignments.get(i).getClassID() == deletedClassID){
                //int index = dataSet.indexOf(assignments.get(i));
                //remove(i);
                dataSet.remove(assignments.get(i));

            }
        }

        for(int i = 0; i < dataSet.size(); i++){
            if(dataSet.get(i) instanceof  AssignmentHeader && i+1 <= dataSet.size()-1 && dataSet.get(i+1) instanceof AssignmentHeader){
                dataSet.remove(i);
            }
        }
        if(dataSet.size() == 1){
            dataSet.remove(0);
        }

    }
}
