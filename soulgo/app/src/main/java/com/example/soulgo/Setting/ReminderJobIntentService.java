package com.example.soulgo.Setting;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.soulgo.R;

public class ReminderJobIntentService extends JobIntentService {

    static final int JOB_ID = 1000;

    // Enqueue work to the JobIntentService.
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, ReminderJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        Log.d("ReminderJobIntentService", "Received work");

        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notifyLemubit")
                .setContentTitle("汪汪 汪 汪汪汪汪 嗚 嗚嗚")
                .setContentText("該上線囉 狗狗們需要你的陪伴 . . .")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(200, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

