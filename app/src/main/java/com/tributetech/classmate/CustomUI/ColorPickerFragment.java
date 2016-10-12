package com.tributetech.classmate.customui;

/**
 * Created by Donnie Propst on 5/13/2016.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.tributetech.classmate.R;
import com.tributetech.classmate.customui.ColorPickerDismissListener;

import java.util.ArrayList;
import java.util.Collection;



/**
 * Created by Donnie Propst on 2/26/2016.
 */
public class ColorPickerFragment extends DialogFragment implements View.OnClickListener{

    private ArrayList<String> colors = new ArrayList<>();
    private String selectedColor;


    private ColorPickerDismissListener listener;


    public ColorPickerFragment(){

    }

    public static ColorPickerFragment newInstance() {
        ColorPickerFragment frag = new ColorPickerFragment();
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select A Color");
        builder.setNegativeButton("Cancel", null);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_color_dialog, null);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                positionButtons(view);
            }
        });

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void positionButtons(View v){
        //This inflates the row layout and adds the row to the parent
        RelativeLayout parent = (RelativeLayout)v.findViewById(R.id.fragment_color_dialog);
        LayoutInflater inflater = LayoutInflater.from(v.getContext());
        View inflatedLayout = inflater.inflate(R.layout.color_row, parent, false);
        parent.addView(inflatedLayout);

        for(int i = 0; i < colors.size() ; i++){
            int position = i % 3;
            int row = i / 3;
            int h = 285;

            FloatingActionButton b = new FloatingActionButton(inflatedLayout.getContext());
            b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(colors.get(i))));

            if(colors.get(i).equals(selectedColor)){
                b.setImageDrawable(ContextCompat.getDrawable(b.getContext(), R.drawable.white_done));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                b.setElevation(0f);
            }
            final int index = i;
            b.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    setSelectedIndex(index);
                }
            });
            ((ViewGroup) inflatedLayout).addView(b);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)b.getLayoutParams();

            switch(position){
                case 0:
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_START);
                    params.setMargins(h/2,row*h,0,25);

                    break;
                case 1:
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.setMargins(0,row*h,0,0);
                    break;
                case 2:
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);
                    params.setMargins(0,row*h,h/2,25);
                    break;
            }
            b.setLayoutParams(params);

        }

    }

    private void setSelectedIndex(int i){
        setSelectedColor(colors.get(i));
        dismiss();
        listener.onComplete();
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    public void setCallbackListener(ColorPickerDismissListener listener){
        this.listener = listener;
    }

    public void setSelectedColor(String color){
        selectedColor = color;
    }

    public String getSelectedColor(){
        return selectedColor;
    }

    public void setColors(String[] colors){
        for(String s : colors){
            this.colors.add(s);
        }
    }


}

