package com.tributetech.classmate.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tributetech.classmate.R;
import com.tributetech.classmate.carriages.AssignmentCarriage;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.domain.Assignment;
import com.tributetech.classmate.domain.AssignmentBuilder;
import com.tributetech.classmate.domain.Class;
import com.tributetech.classmate.utils.CalendarUtils;
import com.tributetech.classmate.utils.ColorReveal;
import com.tributetech.classmate.utils.ColorUtils;
import com.tributetech.classmate.utils.Time;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddAssignment extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    @Bind(R.id.assignment_name_text_field)EditText assignmentNameTextField;
    @Bind(R.id.add_assignment_class_spinner)AppCompatSpinner classNameSpinner;
    @Bind(R.id.assignment_due_date)TextView dueDateTextView;
    @Bind(R.id.add_assignment_due_time_picker)TextView dueTimeTextView;
    @Bind(R.id.remind_checkbox)CheckBox remindCheckbox;
    @Bind(R.id.assignment_description_text_field) EditText descriptionTextField;

    private DatabaseHelper db;
    private ArrayList<Class> classes;
    private AssignmentBuilder assignmentBuilder = new AssignmentBuilder();

    private Assignment carried;

    private String color;

    private boolean updateMode = false;

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        updateDate(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute, int second) {
        updateTime(hourOfDay, minute);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            addAssignment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        db = new DatabaseHelper(this);
        ButterKnife.bind(this);
        classes = db.getAllClasses();
        carried = AssignmentCarriage.getInstance().getToCarry();
        AssignmentCarriage.getInstance().clear();

        if(carried == null){
            setupDefault();
        }else{
            updateMode = true;
            setupCarried();

        }

        //inititateAssignmentBuilderToDefault();
        setupActionBar();
        //if(!classes.isEmpty()) { //throws a null pointer if classes is empty
         //   setColor(classes.get(0).getColor());
        //}
        //assignmentBuilder.addClassID(classes.get(0).getID());
        //setupDatePicker();
        //setupTimePicker();
        //setupClassSpinner();
    }

    private void setupDefault(){
        inititateAssignmentBuilderToDefault();
        setColor(classes.get(0).getColor());
        assignmentBuilder.addClassID(classes.get(0).getID());
        setupDatePicker();
        setupTimePicker();
        setupClassSpinner();
    }

    private void setupCarried(){
        assignmentBuilder = new AssignmentBuilder(carried);
        for(Class c : db.getAllClasses()){
            if(c.getID() == carried.getClassID()) {
                setColor(c.getColor());
            }
        }
        updateColor(color);
        replaceTextValues();
        setupDatePicker();
        setupTimePicker();
        setupClassSpinner();


    }

    private void replaceTextValues(){
        assignmentNameTextField.setText(carried.getAssignmentName());
        remindCheckbox.setChecked(carried.getRemind());
        descriptionTextField.setText(carried.getDescription());
    }

    private void setupClassSpinner() {

        ArrayList<String> classNames = new ArrayList<String>();
        if(classes.size() > 0) {
            for (int i = 0; i < classes.size(); i++) {
                classNames.add(classes.get(i).getClassName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, classNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        classNameSpinner.setAdapter(adapter);
        classNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assignmentBuilder.addClassID(classes.get(i).getID());
                updateColor(classes.get(i).getColor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(updateMode){
          for(int i = 0; i < classes.size(); i++){
              if(classes.get(i).getID() == carried.getClassID()){
                  classNameSpinner.setSelection(i);
              }
          }
        }

    }

    private void updateColor(String color) {
        ColorReveal.change(this, color,(LinearLayout)findViewById(R.id.add_assignment_top_layout));
        this.color = color;
    }

    private void setColor(String color){
         this.color = color;
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ColorUtils.darkenColor(color));
        }
        ActionBar a = getSupportActionBar();
        a.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        LinearLayout ll = (LinearLayout)findViewById(R.id.add_assignment_top_layout);
        ll.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

    }

    private void setupDatePicker(){
        if(updateMode){
            dueDateTextView.setText(CalendarUtils.getFormattedDate(carried.getDueDate()));
        }else {
            dueDateTextView.setText(CalendarUtils.getFormattedDate());
        }
        dueDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddAssignment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setTitle("Select Assignment Due Date");
                dpd.setAccentColor(Color.parseColor(color));
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.dismissOnPause(true);
            }
        });
    }

    private void setupTimePicker(){
        if(updateMode){
            dueTimeTextView.setText(carried.getDueTime().convertToString());
        }else{
            dueTimeTextView.setText(CalendarUtils.getFormattedTime());
        }
        dueTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddAssignment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setTitle("Select Assignment Due Time");
                tpd.setAccentColor(Color.parseColor(color));
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });
    }

    private void updateDate(int year, int month, int date){
        Calendar c = Calendar.getInstance();
        c.set(year, month, date);
        dueDateTextView.setText(CalendarUtils.getFormattedDate(year, month, date));
        assignmentBuilder.addDueDate(c.getTime());
    }
    private void updateTime(int h, int m){
        String time = CalendarUtils.getFormattedTime(h, m);
        dueTimeTextView.setText(CalendarUtils.getFormattedTime(h, m));
        assignmentBuilder.addDueTime(time);

    }

    private void setupActionBar(){
        ActionBar a = getSupportActionBar();
        if(a != null){
            a.setDisplayHomeAsUpEnabled(true);
            a.setDisplayShowTitleEnabled(false);
            a.setTitle("Add Assignment");
            a.setElevation(0f);
        }
    }

    private void cleanValidationErrors() {
        assignmentNameTextField.setError(null);
    }

    private boolean validate(){
        if(assignmentNameTextField.getText().toString().trim().isEmpty()){
            assignmentNameTextField.setError("Assignment name is required");
            return false;
        }
        return true;
    }

    private void inititateAssignmentBuilderToDefault(){
        assignmentBuilder.addAssignmentName("");
        assignmentBuilder.addClassID(0);
        assignmentBuilder.addDueDate(Calendar.getInstance().getTime());
        assignmentBuilder.addDueTime(Time.getNow().toString());
        assignmentBuilder.addReminder(false);
        assignmentBuilder.addComplete(false);
        assignmentBuilder.addDescription("");
    }

    private void addAssignment() {
        cleanValidationErrors();
        if(validate()){
            assignmentBuilder.addAssignmentName(assignmentNameTextField.getText().toString());
            assignmentBuilder.addReminder(remindCheckbox.isChecked());
            assignmentBuilder.addDescription(descriptionTextField.getText().toString());
            Assignment a = assignmentBuilder.build();
            if(updateMode) {
                db.updateAssignment(a);
            }else{
                db.insertAssignment(a);

            }
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
        }
    }

}
