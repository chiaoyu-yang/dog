package com.example.soulgo.Book;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.soulgo.R;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private List<BookModel> data;

    private ItemClickListener itemClickListener;

    public BookAdapter(List<BookModel> data) {
        this.data = data;
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(String bookId);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position + 3 < data.size()) {
            BookModel book = data.get(position + 3); // 從第四筆資料開始顯示

            holder.t1.setText(book.getName());

            // 使用 Glide 載入圖片，並提供佔位圖和錯誤圖片
            Glide.with(holder.img.getContext())
                    .load("http://140.131.114.145/Android/112_dog/books/" + book.getImage())
                    .error(R.drawable.error_image) // 載入錯誤時顯示的圖片
                    .into(holder.img);

            // 如果名字長度小於 3，隱藏item
            if (book.getName().length() < 3) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        } else {
            // 如果超出範圍，隱藏item
            holder.itemView.setVisibility(View.GONE);
        }
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            BookModel book = data.get(position + 3);
                            itemClickListener.onItemClick(book.getId());
                        }
                    }
                }
            });
        }
    }
}
