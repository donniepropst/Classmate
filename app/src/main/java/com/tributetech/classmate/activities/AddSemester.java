package com.tributetech.classmate.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tributetech.classmate.R;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.data.DatabaseUpdateCallback;
import com.tributetech.classmate.domain.Semester;
import com.tributetech.classmate.utils.CalendarUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.Calendar;

public class AddSemester extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    @Bind(R.id.semester_name_text_field) EditText semesterNameEditText;
    @Bind(R.id.add_semester_start_text)TextView semesterStartTextView;
    @Bind(R.id.add_semester_end_text)TextView semesterEndTextView;

    private boolean startDateSelected = false;
    private Semester semester;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);
        ButterKnife.bind(this);
        db = new DatabaseHelper(this);
        setupActionBar();
        setupStartDatePicker();
        setupEndDatePicker();
        semester = createEmptySemester();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_favorite) {
            addSemester();
            return true;
        } else if(id == R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        updateDate(year, monthOfYear, dayOfMonth);
    }
    private void updateDate(int year, int month, int date){
        Calendar c = Calendar.getInstance();
        c.set(year, month ,date);

        if(startDateSelected) {
            semesterStartTextView.setText(CalendarUtils.getFormattedDate(year, month, date));
            semester.setStartDate(c.getTime());
        }else{
            semesterEndTextView.setText(CalendarUtils.getFormattedDate(year, month, date));
            semester.setEndDate(c.getTime());
        }

        if(semester.areDatesSet()) {
            if (semester.getEndDate().before(semester.getStartDate())) {
                semesterStartTextView.setTextColor(getResources().getColor(R.color.fhred));
            }else{
               semesterStartTextView.setTextColor(semesterEndTextView.getCurrentTextColor());
            }
        }

    }
    private void addSemester(){
        if(validate()){
            semester.setName(semesterNameEditText.getText().toString());
            semester.print();
            /*db.insertSemester(semester, new DatabaseUpdateCallback() {
                @Override
                public void finished() {
                    Intent i = new Intent(AddSemester.this, HomeActivity.class);
                    startActivity(i);
                }
            });*/

            //finish();

        }
    }

    private Semester createEmptySemester(){
        return new Semester();
    }

    private boolean validate(){
        semesterNameEditText.setError(null);
        boolean noError = true;
        if(semesterNameEditText.getText().toString().trim().isEmpty()){
            semesterNameEditText.setError("Name is required");
            noError = false;
        }
        if(!semester.areDatesSet()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Set the semester start and end dates");
            builder.setPositiveButton("OK", null);
            AlertDialog ad = builder.create();
            ad.show();
            noError = false;
        }
        else if(semester.getEndDate().before(semester.getStartDate())){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Change the start date to be before the end date");
                builder.setPositiveButton("OK", null);
            AlertDialog ad = builder.create();
            ad.show();
            noError = false;
        }
        return noError;
    }

    private void setupActionBar(){
        ActionBar a = getSupportActionBar();
        if(a != null){
            if(!getIntent().getBooleanExtra("fromIntro", false)) {
                a.setDisplayHomeAsUpEnabled(true);
            }
            a.setDisplayShowTitleEnabled(false);
            a.setElevation(0f);
        }
    }
    private void setupStartDatePicker(){
        //semesterStartTextView.setText(CalendarUtils.getFormattedDate());
        semesterStartTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startDateSelected = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddSemester.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.dismissOnPause(true);
            }
        });
    }
    private void setupEndDatePicker(){
        //semesterEndTextView.setText(CalendarUtils.getFormattedDate());
        semesterEndTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateSelected = false;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddSemester.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.dismissOnPause(true);
            }
        });
    }
}
