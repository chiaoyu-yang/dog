package com.example.beauty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class VoteAdapter extends RecyclerView.Adapter<VoteAdapter.ViewHolder> {
    private List<BeautyItem> beautyList;

    public VoteAdapter(List<BeautyItem> beautyList) {
        this.beautyList = beautyList;
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

        if (beautyItem.isLiked()) {
            holder.likeImageView.setImageResource(R.drawable.keji); // 設定為已按讚的圖示
        } else {
            holder.likeImageView.setImageResource(R.drawable.keji_none); // 設定為未按讚的圖示
        }

        // 使用 Glide 載入圖片，並提供佔位圖和錯誤圖片
        Glide.with(holder.dogimg.getContext())
                .load("http://140.131.114.145/Android/112_dog/beauty/" + beautyItem.getImage())
                .fitCenter()
                .error(R.drawable.error_image) // 載入錯誤時顯示的圖片
                .into(holder.dogimg);

        // 設置點擊事件監聽器
        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新資料
                beautyItem.setLiked(!beautyItem.isLiked());
                if (beautyItem.isLiked()) {
                    beautyItem.setLike(beautyItem.getLike() + 1);
                } else {
                    beautyItem.setLike(beautyItem.getLike() - 1);
                }

                // 更新 UI
                notifyDataSetChanged(); // 通知 RecyclerView 更新
            }
        });
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dognickname = itemView.findViewById(R.id.dognickname);
            likes = itemView.findViewById(R.id.likes);
            dogimg = itemView.findViewById(R.id.dogimg);
            likeImageView = itemView.findViewById(R.id.likeImageView);
        }
    }
}
