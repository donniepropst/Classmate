package com.tributetech.classmate.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tributetech.classmate.R;
import com.tributetech.classmate.adapters.AssignmentAdapter;
import com.tributetech.classmate.data.DatabaseHelper;
import com.tributetech.classmate.data.DatabaseUpdateCallback;
import com.tributetech.classmate.fragments.AssignmentListFragment;
import com.tributetech.classmate.fragments.CalendarFragment;
import com.tributetech.classmate.fragments.ClassListFragment;
import com.tributetech.classmate.reminder.ReminderReceiver;

import butterknife.ButterKnife;


public class HomeActivity extends AppCompatActivity {

    DatabaseHelper db;

    private String myAppID= "ca-app-pub-9556678023043463~1963099434";
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private AdView adView;

    private ReminderReceiver alarm = new ReminderReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, myAppID);
        db = new DatabaseHelper(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("firstLaunch", true)){
            Intent i = new Intent(this, IntroActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }



        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setupFloatingActionButtons();

        // Load an ad into the AdMob banner view.
        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template")
                //.addTestDevice(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID))
                .build();

        adView.loadAd(adRequest);

        alarm.setAlarm(this);

    }

    private void setupFloatingActionButtons() {
        final FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.fab_menu);

        final FloatingActionButton programFab1 = new FloatingActionButton(this);
        programFab1.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab1.setLabelText("Add Class");
        programFab1.setImageResource(R.drawable.white_class);
        programFab1.setColorNormal(getResources().getColor(R.color.colorAccent));
        programFab1.setColorPressed(getResources().getColor(R.color.colorPrimaryDark));
        menu.addMenuButton(programFab1);
        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddClass.class));
            }
        });

        final FloatingActionButton programFab2 = new FloatingActionButton(this);
        programFab2.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab2.setLabelText("Add Assignment");
        programFab2.setColorNormal(getResources().getColor(R.color.colorAccent));
        programFab2.setColorPressed(getResources().getColor(R.color.colorPrimaryDark));
        programFab2.setImageResource(R.drawable.white_assignment);
        menu.addMenuButton(programFab2);
        programFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddAssignment.class));
            }
        });
        if (db.getAllClasses().isEmpty()) {
            menu.removeMenuButton(programFab2);
        }
        menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (db.getAllClasses().isEmpty()) {
                    menu.removeMenuButton(programFab2);
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ClassListFragment.newInstance();

                case 1:
                    return AssignmentListFragment.newInstance();

                default:
                    return CalendarFragment.newInstance();
            }

        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Classes";
                case 1:
                    return "Assignments";
                case 2:
                    return "Calendar";
            }
            return null;
        }


    }


}
