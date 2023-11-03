package com.example.soulgo.Beauty;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.soulgo.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {
    private final List<BeautyItem> beautyList;

    private final LikeClickListener likeClickListener;

    private final Context context;

    public VoteAdapter(List<BeautyItem> beautyList, LikeClickListener likeClickListener, Context context) {
        this.beautyList = beautyList;
        this.likeClickListener = likeClickListener;
        this.context = context;
    }

    public interface LikeClickListener {
        void onLikeClick(BeautyItem beautyItem);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beauty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BeautyItem beautyItem = beautyList.get(position);
        holder.dognickname.setText(beautyItem.getName());
        holder.likes.setText(String.valueOf(beautyItem.getLike()));

        boolean isLiked = isLiked(beautyItem.getBeautyId());

        if (LikeStatusManager.shouldResetLikes(context)) {
            clearAllLikeStatus();
            // 重置按讚狀態後，設置為未按讚的圖示
            holder.likeImageView.setImageResource(R.drawable.keji_none);
        } else {
            // 根據按讚狀態設置圖示
            if (isLiked) {
                holder.likeImageView.setImageResource(R.drawable.keji);
            } else {
                holder.likeImageView.setImageResource(R.drawable.keji_none);
            }
        }

        // 使用 Glide 載入圖片，並提供佔位圖和錯誤圖片
        Glide.with(holder.dogimg.getContext())
                .load("http://140.131.114.145/Android/112_dog/beauty/" + beautyItem.getImage())
                .fitCenter()
                .error(R.drawable.error_image) // 載入錯誤時顯示的圖片
                .into(holder.dogimg);

        // 設置點擊事件監聽器
        holder.likeImageView.setOnClickListener(v -> {
            if (isLiked) {
                // 取消按讚
                beautyItem.setLiked(false);
                beautyItem.setLike(beautyItem.getLike() - 1);
                clearLikeStatus(beautyItem.getBeautyId());
            } else {
                // 按讚
                beautyItem.setLiked(true);
                beautyItem.setLike(beautyItem.getLike() + 1);
                saveLikeStatus(beautyItem.getBeautyId());
            }

            notifyDataSetChanged(); // 更新 UI
            if (likeClickListener != null) {
                likeClickListener.onLikeClick(beautyItem);
            }
        });

        holder.cardView.setOnClickListener(v -> {
            // 點擊 CardView，觸發雙擊事件
            if (holder.isDoubleClick) {
                toggleLike(beautyItem, holder);
                holder.isDoubleClick = false; // 重置雙擊狀態
            } else {
                holder.isDoubleClick = true;
                // 設置一個延遲時間來處理單擊事件
                v.postDelayed(() -> {
                    if (holder.isDoubleClick) {
                        holder.isDoubleClick = false; // 重置雙擊狀態
                        // 此處處理單擊事件
                    }
                }, 300); // 設置延遲時間，例如 300 毫秒
            }
        });
    }

    private void toggleLike(BeautyItem beautyItem, ViewHolder holder) {
        boolean isLiked = isLiked(beautyItem.getBeautyId());

        if (isLiked) {
            // 取消按讚
            beautyItem.setLiked(false);
            beautyItem.setLike(beautyItem.getLike() - 1);
            holder.likeImageView.setImageResource(R.drawable.keji_none);
            clearLikeStatus(beautyItem.getBeautyId());
        } else {
            // 按讚
            beautyItem.setLiked(true);
            beautyItem.setLike(beautyItem.getLike() + 1);
            holder.likeImageView.setImageResource(R.drawable.keji);
            saveLikeStatus(beautyItem.getBeautyId());
        }

        notifyDataSetChanged();
        if (likeClickListener != null) {
            likeClickListener.onLikeClick(beautyItem);
        }
    }

    public void clearAllLikeStatus() {
        SharedPreferences preferences = context.getSharedPreferences("LikeStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        for (BeautyItem beautyItem : beautyList) {
            String key = "like_" + beautyItem.getBeautyId();
            editor.putBoolean(key, false);
        }

        editor.apply();
    }

    public void clearLikeStatus(int beautyId) {
        SharedPreferences preferences = context.getSharedPreferences("LikeStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String key = "like_" + beautyId;
        editor.putBoolean(key, false);

        editor.apply();
    }

    @Override
    public int getItemCount() {
        return beautyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dognickname;
        TextView likes;
        ImageView dogimg;
        ImageView likeImageView;
        boolean isDoubleClick = false;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dognickname = itemView.findViewById(R.id.dognickname);
            likes = itemView.findViewById(R.id.likes);
            dogimg = itemView.findViewById(R.id.dogimg);
            likeImageView = itemView.findViewById(R.id.likeImageView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    public void saveLikeStatus(int beautyId) {
        SharedPreferences preferences = context.getSharedPreferences("LikeStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String key = "like_" + beautyId;
        editor.putBoolean(key, true);

        String dateKey = "date_" + beautyId;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        editor.putString(dateKey, currentDate);

        editor.apply();
    }

    public boolean isLiked(int beautyId) {
        SharedPreferences preferences = context.getSharedPreferences("LikeStatus", Context.MODE_PRIVATE);
        String key = "like_" + beautyId;
        return preferences.getBoolean(key, false);
    }
}