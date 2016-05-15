package com.avv.scrumtimer.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.util.ArrayList;

/**
 * Created by angelvazquez on 14/5/16.
 */


public class NumberPickerPreference extends DialogPreference {

    // enable or disable the 'circular behavior'
    public static final boolean WRAP_SELECTOR_WHEEL = true;


    NumberPicker shiftTimeMinute;
    NumberPicker shiftTimeSecond;
    private long value;

    String[] minutes;
    String[] seconds;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View onCreateDialogView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        shiftTimeMinute = new NumberPicker(getContext());
        shiftTimeMinute.setLayoutParams(layoutParams);

        shiftTimeSecond = new NumberPicker(getContext());
        shiftTimeSecond.setLayoutParams(layoutParams);

        LinearLayout dialogView = new LinearLayout(getContext());
        dialogView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogView.setOrientation(LinearLayout.HORIZONTAL);
        dialogView.setGravity(Gravity.CENTER);
        dialogView.addView(shiftTimeMinute);
        dialogView.addView(shiftTimeSecond);

        return dialogView;
    }

    private String[] loadMinuteValues(){
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<11;i++) {
            list.add(String.format("%02d",i));
        }
        String[] hours = new String[list.size()];
        return list.toArray(hours);

    }

    private String[] loadSecondValues(){
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<59;i=i+5) {
            list.add(String.format("%02d",i));
        }
        String[] minutes = new String[list.size()];
        return list.toArray(minutes);

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        long time = getValue();


        int minutesValue = (int) (time / 1000) / 60;
        int secondsValue = (int) (time / 1000) % 60;

        shiftTimeMinute.setMinValue(0);
        minutes = loadMinuteValues();
        shiftTimeMinute.setMaxValue(minutes.length - 1);
        shiftTimeMinute.setDisplayedValues(minutes);
        shiftTimeMinute.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        shiftTimeMinute.setValue(minutesValue);


        shiftTimeSecond.setMinValue(0);
        seconds = loadSecondValues();


        secondsValue = (secondsValue * 5) % seconds.length;
        shiftTimeSecond.setMaxValue(seconds.length - 1);
        shiftTimeSecond.setDisplayedValues(seconds);
        shiftTimeSecond.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        shiftTimeSecond.setValue(secondsValue);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            shiftTimeMinute.clearFocus();
            shiftTimeSecond.clearFocus();
            int newValueMinutes = shiftTimeMinute.getValue();
            int newValueSeconds = Integer.valueOf(seconds[shiftTimeSecond.getValue()]);
            long newValue = (60 * newValueMinutes + newValueSeconds)*1000;
            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedLong(0) : (Long) defaultValue);
    }

    public void setValue(long value) {
        this.value = value;
        persistLong(this.value);
    }

    public long getValue() {
        return this.value;
    }
}