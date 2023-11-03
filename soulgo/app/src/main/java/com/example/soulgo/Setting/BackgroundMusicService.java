package com.example.soulgo.Setting;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.soulgo.R;

public class BackgroundMusicService extends Service {
    private static MediaPlayer mediaPlayer;
    private static float volume = 0.5f; // Default volume

    public static void setVolume(float newVolume) {
        volume = newVolume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume, volume);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.dog);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(volume, volume); // 设置初始音量
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
