package com.example.soulgo.Setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ReminderBroadcast", "Received broadcast");

        // Replace the Notification code with starting the JobIntentService
        ReminderJobIntentService.enqueueWork(context, intent);
    }
}
