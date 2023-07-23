package com.example.soulgo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private List<BookModel> data;

    public BookAdapter(List<BookModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookModel book = data.get(position);
        holder.t1.setText(book.getName());

        // 使用 Glide 載入圖片，並提供佔位圖和錯誤圖片
        Glide.with(holder.img.getContext())
                .load("http://140.131.114.145/Android/112_dog/books/" + book.getImage())
                .error(R.drawable.error_image) // 載入錯誤時顯示的圖片
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView t1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.booklist_image_button);
            t1 = itemView.findViewById(R.id.booklist_text);
        }
    }
}
