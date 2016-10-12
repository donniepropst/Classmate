package com.tributetech.classmate.activities;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tributetech.classmate.customui.ColorPickerDismissListener;
import com.tributetech.classmate.customui.ColorPickerFragment;
import com.tributetech.classmate.customui.DayMultiSpinner;
import com.tributetech.classmate.R;
import com.tributetech.classmate.customui.SemesterSpinner;
import com.tributetech.classmate.carriages.ClassCarriage;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.domain.*;
import com.tributetech.classmate.domain.Class;
import com.tributetech.classmate.utils.CalendarUtils;
import com.tributetech.classmate.utils.ColorReveal;
import com.tributetech.classmate.utils.Time;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AddClass extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{


    @Bind(R.id.add_class_start_time_picker)TextView startTimeTextView;
    @Bind(R.id.add_class_end_time_picker)TextView endTimeTextView;
    @Bind(R.id.class_name_text_field)EditText classNameEditText;
    @Bind(R.id.add_class_location_text_field) EditText classLocationEditText;
    //@Bind(R.id.add_class_semester_spinner)SemesterSpinner semesterSpinner;
    @Bind(R.id.add_class_days_text)TextView dayText;
    @Bind(R.id.color_lens_color_indicator)ImageView colorIndicator;

    private DayMultiSpinner dayDialog;

    private boolean startTime;
    private RelativeLayout colorSelectorRow;
    private RelativeLayout startTimeRow;
    private RelativeLayout endTimeRow;
    private RelativeLayout addClassDayLayout;

    private ClassBuilder classBuilder = new ClassBuilder();
   // private ArrayList<Semester> semesterList;

    private boolean updateMode = false;


    private DatabaseHelper db;
    private Class carried;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        ButterKnife.bind(this);
        setupActionBar();
        db = new DatabaseHelper(this);
       // semesterList = db.getAllSemesters();
        carried = ClassCarriage.getInstance().getToCarry();
        ClassCarriage.getInstance().clear();
        if(carried == null){
            setupDefault();
        }else{
            setupCarried();
            updateMode = true;
        }

    }

    private void setupDefault(){
        setupStartTimePicker();
        setupEndTimePicker();
        //setupSemesterRow();
        setupColorRow();
        setupDateRow();
        initiateClassBuilderToDefault();
    }
    private void setupCarried(){
        setupHeaderLayout();
        classBuilder = new ClassBuilder(carried);
        setupStartTimePicker(carried.getStartTime());
        setupEndTimePicker(carried.getEndTime());
        setupColorRow(carried.getColor());
        updateColors(carried.getColor());
        //setupSemesterRow(carried.getSemesterID());
        setupDateRow(carried.getDays());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            addClass();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        updateTime(hourOfDay, minute);
    }

    private void setupStartTimePicker(Time... time){
        Time temp;
        if(time.length > 0)
            temp = time[0];
        else
            temp = Time.getNow();

        final Time classStartTime = temp;
        startTimeTextView.setText("Class Start Time     " + classStartTime.toString());
        startTimeRow = (RelativeLayout)findViewById(R.id.startTimeLayout);
        startTimeRow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startTime = true;
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddClass.this,
                        classStartTime.getHour(),
                        classStartTime.getMinute(),
                        false
                );
                tpd.setTitle("Set Class Start Time");
                tpd.setAccentColor(Color.parseColor(classBuilder.getColor()));
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

    }
    private void setupEndTimePicker(Time... time){
        Time temp;
        if(time.length > 0) {
            temp = time[0];
        }else {
            temp = Time.getHourAdvanced();
        }

        final Time classEndTime = temp;
        endTimeTextView.setText("Class End Time       " + classEndTime.convertToString());
        endTimeRow = (RelativeLayout)findViewById(R.id.endTimeLayout);
        endTimeRow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startTime = false;
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddClass.this,
                        classEndTime.getHour(),
                        classEndTime.getMinute(),
                        false
                );
                tpd.setTitle("Set Class End Time");
                tpd.setAccentColor(Color.parseColor(classBuilder.getColor()));
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }
    private void updateTime(int h, int m){
        if(startTime) {
            String time = CalendarUtils.getFormattedTime(h, m);
            startTimeTextView.setText("Class Start Time     " + time);
            classBuilder.addStartTime(CalendarUtils.getDateTimeObject(h, m));
        }else{
            String time = CalendarUtils.getFormattedTime(h, m);
            endTimeTextView.setText("Class End Time       " + time);
            classBuilder.addEndTime(CalendarUtils.getDateTimeObject(h, m));
        }
    }

    private void setupHeaderLayout(){
        classNameEditText.setText(carried.getClassName());
        classLocationEditText.setText(carried.getRoom());
    }

    private void addClass(){
            cleanValidationErrors();
            if(validate()){
                classBuilder.addClassName(classNameEditText.getText().toString());
                classBuilder.addClassRoom(classLocationEditText.getText().toString());
                classBuilder.setDays(dayDialog.getSelectedArray());
                //classBuilder.setSemesterID(semesterSpinner.getSelectedSemester().getID());

                Class c = classBuilder.build();

                if(!updateMode){
                    db.insertClass(c);
                }else {
                    c.setID(carried.getID());
                    db.updateClass(c);
                }
                Intent i = new Intent(this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);

            }
    }

    private boolean validate(){
        if(classNameEditText.getText().toString().trim().isEmpty()){
            classNameEditText.setError("Class name is required");
            return false;
        }
        return true;
    }
    private void cleanValidationErrors(){
        classNameEditText.setError(null);
    }

    private void initiateClassBuilderToDefault(){
        classBuilder.addClassName("");
        classBuilder.addClassRoom("");
        classBuilder.addClassDays("0");
        classBuilder.addStartTime(CalendarUtils.getDateTimeObject(Time.getNow().getHour(), Time.getNow().getMinute()));
        classBuilder.addEndTime(CalendarUtils.getDateTimeObject(Time.getHourAdvanced().getHour(), Time.getHourAdvanced().getMinute()));
        classBuilder.addColor("#03A9F4");
    }

    private void setupActionBar(){
        ActionBar a = getSupportActionBar();
        if(a != null){
            a.setDisplayHomeAsUpEnabled(true);
            a.setDisplayShowTitleEnabled(false);
            a.setElevation(0f);
        }
    }
    private void updateColors(String color) {
        classBuilder.addColor(color);
        colorIndicator.setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);
        ColorReveal.change(this, color, (LinearLayout) findViewById(R.id.add_class_top_layout));

    }

    private void showDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        final ColorPickerFragment colorFragment = ColorPickerFragment.newInstance();
        String[] colors = getResources().getStringArray(R.array.color_array);

        colorFragment.setColors(colors);

        colorFragment.setSelectedColor(classBuilder.getColor());

        colorFragment.setCallbackListener(new ColorPickerDismissListener() {
            @Override
            public void onComplete() {
                updateColors(colorFragment.getSelectedColor());
            }
        });

        colorFragment.show(ft, "dialog");


    }
    private void setupColorRow(String... color){
        String layoutColor = "#03A9F4";
        if(color.length > 0)
            layoutColor = color[0];
        colorIndicator.setColorFilter(Color.parseColor(layoutColor), PorterDuff.Mode.MULTIPLY);
        colorSelectorRow = (RelativeLayout)findViewById(R.id.add_class_color_layout);
        colorSelectorRow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDialog();
            }
        });

    }

    /*private void setupSemesterRow(int... semester){
        semesterSpinner.init(semesterList);
        if(semester.length > 0) {
            semesterSpinner.setPreselected(semester[0]);
        }
    }*/

    private void setupDateRow(String... days){
        dayDialog = new DayMultiSpinner(this, dayText);
        addClassDayLayout = (RelativeLayout)findViewById(R.id.add_class_days_layout);
        addClassDayLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dayDialog.init(classBuilder.getColor());
            }
        });

      if(days.length > 0){
          dayDialog.update(days[0]);
      }else{
          dayDialog.defaultText();
      }

    }

}
