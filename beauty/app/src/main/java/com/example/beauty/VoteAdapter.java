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

        // 使用 Glide 載入圖片，並提供佔位圖和錯誤圖片
        Glide.with(holder.dogimg.getContext())
                .load("http://140.131.114.145/Android/112_dog/beauty/" + beautyItem.getImage())
                .error(R.drawable.error_image) // 載入錯誤時顯示的圖片
                .into(holder.dogimg);
    }

    @Override
    public int getItemCount() {
        return beautyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dognickname;
        TextView likes;
        ImageView dogimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dognickname = itemView.findViewById(R.id.dognickname);
            likes = itemView.findViewById(R.id.likes);
            dogimg = itemView.findViewById(R.id.dogimg);
        }
    }
}
