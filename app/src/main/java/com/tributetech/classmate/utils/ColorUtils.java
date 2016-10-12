package com.tributetech.classmate.utils;

import android.graphics.Color;

/**
 * Created by Donnie Propst on 2/28/2016.
 */
public class ColorUtils {


    public static int darkenColor(String color){
        float[] hsv = new float[3];
        int c = Color.parseColor(color);
        Color.colorToHSV(c, hsv);
        hsv[2] *= 0.8f; // value component
        return Color.HSVToColor(hsv);
    }

}
