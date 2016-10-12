package com.tributetech.classmate.customui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tributetech.classmate.R;

import java.util.ArrayList;

public class DayMultiSpinner{
    private ArrayList<String> dayList;
    private MaterialDialog dialog;
    private Context context;
    private TextView dayText;
    private boolean[] selectedItems;


    public DayMultiSpinner(Context c, TextView dayText){
        context = c;
        defaultDayNames();
        this.dayText = dayText;
        selectedItems = new boolean[7];
    }


    public void init(String color) {

        dialog = new MaterialDialog.Builder(context)
                .title("Select Class Days")
                .items(dayList)
                .itemsCallbackMultiChoice(getSelectedIndices(), new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        updateChoices(which);
                        if(which.length > 0) {
                            update(getSelected());
                        }else{
                            defaultText();
                        }
                        return true;
                    }
                })
                .widgetColor(Color.parseColor(color))
                .cancelable(true)
                .negativeColor(context.getResources().getColor(R.color.secondary_text))
                .positiveColor(context.getResources().getColor(R.color.secondary_text))
                .negativeText("Cancel")
                .positiveText("Done")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void defaultDayNames(){
        dayList = new ArrayList<>();
        dayList.add("Monday");
        dayList.add("Tuesday");
        dayList.add("Wednesday");
        dayList.add("Thursday");
        dayList.add("Friday");
        dayList.add("Saturday");
        dayList.add("Sunday");
    }

    public void update(String day){
        StringBuilder dayBlockText = new StringBuilder();

        for(char c : day.toCharArray()){
            dayBlockText.append((dayBlockText.toString().trim().isEmpty() ? "" : ", ")+ dayList.get(Character.getNumericValue(c)).substring(0,3));
            selectedItems[Character.getNumericValue(c)] = true;
        }

        setSelected(selectedItems);

        setText("Days: " + dayBlockText.toString());

    }

    public void defaultText(){
        setText("Set Days:");
    }

    private void setSelected(boolean selected[]){
        selectedItems = selected;
    }

    private void setText(String text){
        dayText.setText(text);
    }

    private void updateChoices(Integer choices[]){
        selectedItems = new boolean[7];
        for(int i : choices){
            System.out.println("SELECTED: " + i);
            selectedItems[i] = true;
        }
    }

    private Integer[] getSelectedIndices(){
        ArrayList<Integer> selectedTemp = new ArrayList<>();
        for(int i = 0; i < selectedItems.length; i++){
            if(selectedItems[i])
                selectedTemp.add(i);
        }
        return selectedTemp.toArray(new Integer[selectedTemp.size()]);
    }

    private String getSelected(){
        StringBuilder builder = new StringBuilder();
        ArrayList<Integer> selectedTemp = new ArrayList<>();
        for(int i = 0; i < selectedItems.length; i++){
            if(selectedItems[i])
               builder.append(i);
        }
        return builder.toString();
    }

    public boolean[] getSelectedArray(){
        return selectedItems;
    }
}
