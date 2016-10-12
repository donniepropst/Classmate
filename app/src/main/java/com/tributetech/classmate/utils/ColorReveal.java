package com.tributetech.classmate.utils;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;

import com.tributetech.classmate.R;

/**
 * Created by Donnie Propst on 5/5/2016.
 */
public class ColorReveal{

    public static void change(AppCompatActivity currentActivity, String color, LinearLayout parent){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            currentActivity.getWindow().setStatusBarColor(ColorUtils.darkenColor(color));
        }
        ActionBar a = currentActivity.getSupportActionBar();
        a.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        //LinearLayout ll = (LinearLayout)currentActivity.findViewById(R.id.add_class_top_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            parent.setVisibility(View.INVISIBLE);
        }

        parent.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // previously invisible view
                //View myView = currentActivity.findViewById(R.id.add_class_top_layout);
                // get the center for the clipping circle
                int cx = parent.getWidth() / 2;
                int cy = parent.getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(parent, cx, cy, 0, finalRadius);

                // make the view visible and start the animation
                parent.setVisibility(View.VISIBLE);
                anim.start();
            }
        }catch(IllegalStateException e){
            parent.setVisibility(View.VISIBLE);
        }
    }

}
