package com.ryx.triggernotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.app.Notification.DEFAULT_ALL;
import static android.graphics.Color.RED;

public class NotifyWork extends Worker {

    private static final String TAG = "NotifyWork";

    public static String NOTIFICATION_ID = "appName_notification_id";
    public static String NOTIFICATION_NAME = "appName";
    public static String NOTIFICATION_CHANNEL = "appName_channel_01";
    public static String NOTIFICATION_WORK = "appName_notification_work";

    public NotifyWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: called");
        int id = (int) getInputData().getLong(NOTIFICATION_ID, 0);
        sendNotification(id);
        return Result.success();
    }

    private void sendNotification(int id) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, id);

        NotificationManager manager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat
                .Builder(getApplicationContext(), NOTIFICATION_CHANNEL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification Trigger")
                .setContentText("The notification is triggered")
                .setDefaults(DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL);

            Uri ringToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            long[] pattern = new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400};

            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(pattern);
            notificationChannel.setSound(ringToneUri, attributes);
            manager.createNotificationChannel(notificationChannel);
        }

        manager.notify(id, notification.build());

    }
}
