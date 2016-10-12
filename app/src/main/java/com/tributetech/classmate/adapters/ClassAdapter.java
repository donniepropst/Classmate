package com.tributetech.classmate.adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tributetech.classmate.R;
import com.tributetech.classmate.activities.HomeActivity;
import com.tributetech.classmate.activities.MainActivity;
import com.tributetech.classmate.customui.ColorPickerDismissListener;
import com.tributetech.classmate.customui.ColorPickerFragment;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.domain.Assignment;
import com.tributetech.classmate.domain.Class;
import com.tributetech.classmate.fragments.AssignmentListFragment;
import com.tributetech.classmate.fragments.ClassAssignmentDialog;
import com.tributetech.classmate.utils.BottomSheet;
import com.tributetech.classmate.utils.CalendarUtils;

import java.util.ArrayList;


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> implements ModifiableAdapter{

    private Fragment frag;

    @Override
    public void remove(int index) {
        classes.remove(index);
        notifyDataSetChanged();
    }


    private ArrayList<Class> classes;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView className;
        public TextView location;
        public ImageView assignmentButtonIcon;
        public TextView dayText;

        public CardView card;
        public ViewHolder(View v) {
            super(v);
            className = (TextView)v.findViewById(R.id.card_class_title);
            location = (TextView)v.findViewById(R.id.card_class_location);
            dayText = (TextView)v.findViewById(R.id.card_class_days);
            assignmentButtonIcon = (ImageView)v.findViewById(R.id.class_assignment_button);
            card = (CardView)v.findViewById(R.id.class_card);
        }
    }

    public ClassAdapter(ArrayList<Class> classes, Fragment frag) {
        this.classes = classes;
        BottomSheet.classAdapterReference = this;
        this.frag = frag;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.className.setText(classes.get(position).getClassName());
        holder.location.setText(classes.get(position).getRoom());
        String separator = " • ";
        if(classes.get(position).getDays().length() == 0){
            separator = "";
        }
        holder.dayText.setText(CalendarUtils.formatDay(classes.get(position).getDays()) + separator + classes.get(position).getStartTime() + " - " + classes.get(position).getEndTime() );
        holder.assignmentButtonIcon.setColorFilter(Color.parseColor(classes.get(position).getColor()));
        holder.assignmentButtonIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
              showDialog(view, classes.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                    BottomSheet bs = new BottomSheet(holder.itemView, classes.get(position).getID(), position, classes.get(position));
                    bs.createBottomDialog();
                    return true;
            }
        });

        if(position == classes.size()-1){
            holder.itemView.setPadding(0, 0, 0, 500);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return classes.size();
    }

    private void showDialog(View v, Class c){

            FragmentTransaction ft = frag.getActivity().getFragmentManager().beginTransaction();
            final ClassAssignmentDialog classAssignmentDialog = ClassAssignmentDialog.newInstance();
                classAssignmentDialog.setClass(c);

            classAssignmentDialog.show(ft, "assignment_dialog");
    }
}
