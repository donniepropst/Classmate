package com.tributetech.classmate.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tributetech.classmate.R;
import com.tributetech.classmate.activities.AddAssignment;
import com.tributetech.classmate.activities.AddClass;
import com.tributetech.classmate.activities.HomeActivity;
import com.tributetech.classmate.adapters.AssignmentAdapter;
import com.tributetech.classmate.adapters.ClassAdapter;
import com.tributetech.classmate.adapters.ModifiableAdapter;
import com.tributetech.classmate.carriages.AssignmentCarriage;
import com.tributetech.classmate.carriages.ClassCarriage;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.domain.Assignment;
import com.tributetech.classmate.domain.Class;
import com.tributetech.classmate.fragments.AssignmentListFragment;

/**
 * Created by Donnie Propst on 2/28/2016.
 */
public class BottomSheet extends Dialog{

    public static RecyclerView.Adapter classAdapterReference;
    public static RecyclerView.Adapter assignmentAdapterReference;

    private Dialog mBottomSheetDialog;

    private TextView deleteTextView;
    private TextView editTextView;


    private View v;
    private int ID;
    private int position;
    private Object toCarry;

    public <T>BottomSheet(View v, int ID, int position, Object toCarry){
        super(v.getContext());
        this.v = v;
        this.ID = ID;
        this.position = position;
        this.toCarry = toCarry;
    }
    public void createBottomDialog(){

        mBottomSheetDialog = new Dialog (v.getContext(), R.style.MaterialDialogSheet);
        View sheet = View.inflate(v.getContext(), R.layout.dialog_class_bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheet);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show();
        bindViews(sheet);
    }

    private void bindViews(final View sheet){
        deleteTextView = (TextView)sheet.findViewById(R.id.bottom_sheet_dialog_delete);
        deleteTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final DatabaseHelper helper = new DatabaseHelper(sheet.getContext());
                if(toCarry instanceof Class) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(sheet.getContext())
                            .setTitle("Important!")
                            .setMessage("Deleting a class will also delete all associated assignments.\nAre you sure you want to proceed?")
                            .setPositiveButton("Yes", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    helper.deleteClass(ID);
                                    ((ModifiableAdapter) classAdapterReference).remove(position);
                                    try{
                                    ((AssignmentAdapter)assignmentAdapterReference).removeClass(ID);
                                            assignmentAdapterReference.notifyDataSetChanged();
                                     }catch(NullPointerException e){
                                        //no assignment adapter yet
                                    }
                                    dismiss();
                                    mBottomSheetDialog.dismiss();

                                }
                            })
                            .setNegativeButton("No", null);
                    builder.show();
                }else{
                    helper.deleteAssignment(ID);
                    ((ModifiableAdapter) assignmentAdapterReference).remove(position);
                    dismiss();
                    mBottomSheetDialog.dismiss();
                }

            }
        });
        editTextView = (TextView)sheet.findViewById(R.id.bottom_sheet_dialog_edit);
        editTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(toCarry instanceof Class){
                    ClassCarriage.getInstance().setToCarry((Class)toCarry);
                    Intent i = new Intent(view.getContext(), AddClass.class);
                    view.getContext().startActivity(i);
                }else{
                    AssignmentCarriage.getInstance().setToCarry((Assignment)toCarry);
                    Intent i = new Intent(view.getContext(), AddAssignment.class);
                    view.getContext().startActivity(i);
                }

                mBottomSheetDialog.dismiss();
            }
        });
    }

}
