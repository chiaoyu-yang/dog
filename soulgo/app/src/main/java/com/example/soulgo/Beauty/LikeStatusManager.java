package com.example.soulgo.Beauty;

import android.content.Context;
import android.content.SharedPreferences;

public class LikeStatusManager {
    private static final String PREFERENCES_NAME = "LikeStatus";
    private static final String LAST_RESET_TIME_KEY = "last_reset_time";

    public static boolean shouldResetLikes(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        long lastResetTime = preferences.getLong(LAST_RESET_TIME_KEY, 0);

        // 比較當前時間是否已經超過00:00
        long currentTime = System.currentTimeMillis();
        // 假設您要在每天的00:00重置，那麼您需要計算當前日期的00:00的時間戳
        // 這裡假設您希望在每天00:00重置，可以根據需要進行調整
        long midnightTime = calculateMidnightTime();

        if (currentTime >= midnightTime && lastResetTime < midnightTime) {
            // 如果當前時間超過00:00並且上次重置時間在00:00之前，表示需要重置按讚狀態
            preferences.edit().putLong(LAST_RESET_TIME_KEY, currentTime).apply();
            return true;
        } else {
            return false;
        }
    }

    private static long calculateMidnightTime() {
        // 計算當前日期的00:00時間戳
        // 可以使用Calendar類或其他方法來實現
        // 這裡提供一個簡單的示例
        // 假設今天是2023-10-27，計算出的時間戳將是2023-10-27 00:00:00的毫秒數
        // 請根據實際需求進行調整
        // 請注意，下面的示例可能不處理時區問題，可能需要進一步優化
        return System.currentTimeMillis() - (System.currentTimeMillis() % (24 * 60 * 60 * 1000));
    }
}