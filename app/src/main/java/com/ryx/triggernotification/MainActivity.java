package com.ryx.triggernotification;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.ryx.triggernotification.NotifyWork.NOTIFICATION_ID;
import static com.ryx.triggernotification.NotifyWork.NOTIFICATION_WORK;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DatePicker datePicker;
    private CustomTimePicker timePicker;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSchedule();
            }
        });

    }

    private void initView() {
        datePicker = findViewById(R.id.date_p);
        timePicker = findViewById(R.id.time_p);
        fab = findViewById(R.id.done_fab);
    }

    private void setSchedule() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                timePicker.getHour(), timePicker.getMinute(), 0
        );
        Long customTime = calendar.getTimeInMillis();
        Long currentTime = System.currentTimeMillis();

        if (customTime > currentTime) {
            Data data = new Data.Builder().putInt(NOTIFICATION_ID, 0).build();
            Long delay = customTime - currentTime;

            setNotification(delay, data);
            Toast.makeText(this, "Reminder set!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Set reminder at different time", Toast.LENGTH_SHORT).show();
        }
    }

    private void setNotification(Long delay, Data data) {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotifyWork.class)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build();

        WorkManager manager = WorkManager.getInstance(this);
        manager.beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.REPLACE, request).enqueue();
    }
}
