package com.example.soulgo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class Beep extends Service {
    private static MediaPlayer mediaPlayer;

    private static float beep = 0.5f; // Default volume

    public static void setBeep(float newBeep) {
        beep = newBeep;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(beep, beep);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.beep);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(beep, beep); // 设置初始音量
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return START_STICKY; // 使服务在被系统杀死后能够自动重启
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
