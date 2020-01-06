package com.ryx.triggernotification;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TimePicker;

public class CustomTimePicker extends TimePicker {
    public CustomTimePicker(Context context) {
        super(context);
    }

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getHour() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return super.getHour();
        else return super.getCurrentHour();
    }

    @Override
    public void setHour(int hour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.setHour(hour);
        else super.setCurrentHour(hour);
    }

    @Override
    public int getMinute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return super.getMinute();
        else return super.getCurrentMinute();
    }

    @Override
    public void setMinute(int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            super.setMinute(minute);
        else super.setCurrentMinute(minute);
    }
}
