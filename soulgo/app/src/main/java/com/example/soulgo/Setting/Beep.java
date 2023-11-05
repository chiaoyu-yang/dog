package com.example.soulgo.Setting;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.soulgo.R;

public class Beep {
    private static float volume = 1.0f; // 默认音量为1.0

    public static void playBeepSound(Context context) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.beep);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.setVolume(volume, volume); // 设置音量
        mediaPlayer.start();
    }

    public static void setVolume(float newVolume) {
        volume = newVolume;
    }
}


