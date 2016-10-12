package com.tributetech.classmate.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.tributetech.classmate.R;

/**
 * Created by Donnie Propst on 5/16/2016.
 */
public class IntroActivity extends AppIntro{

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.LTGRAY);
        }
        addSlide(AppIntroFragment.newInstance("Welcome to ClassMate", "Congrats on taking the first step to managing your education the modern way.  Use ClassMate to easily keep track of all your classes and assignments in one place.", R.drawable.book, Color.parseColor("#75DDDD")));
        addSlide(AppIntroFragment.newInstance("Organization Made Easy", "Never miss a due date again!  \n Log assignments for each class, track their due dates in the monthly calendar, and receive notifications before they are due.", R.drawable.alarm_clock, Color.parseColor("#88E881")));
        addSlide(AppIntroFragment.newInstance("You're All Set!", "Just add a class to get started.  Happy studying!", R.drawable.desk, Color.parseColor("#E01A4F")));


        setSeparatorColor(Color.parseColor("#00FFFFFF"));
        showSkipButton(false);


    }

    @Override
    public void onSkipPressed() {

    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onDonePressed() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putBoolean("firstLaunch", false);
        editor.apply();
        editor.commit();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    @Override
    public void onSlideChanged() {

    }



}
