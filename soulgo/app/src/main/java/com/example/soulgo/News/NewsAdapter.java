package com.example.soulgo.News;

import android.content.Context;
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

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private List<NewsModel> data;
    private Context context;
    private ItemClickListener itemClickListener;

    public NewsAdapter(Context context, List<NewsModel> data) {
        this.context = context;
        this.data = data;
    }


    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(String newsId);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewsModel news = data.get(position);
        holder.newsTitle.setText(news.getTitle());
        holder.newsContent.setText(news.getContent());

        Glide.with(holder.img.getContext())
                .load("http://140.131.114.145/Android/112_dog/news/" + news.getImage())
                .error(R.drawable.error_image)
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView newsTitle, newsContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.booklist_image_button);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsContent = itemView.findViewById(R.id.newsContent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            NewsModel news = data.get(position);
                            itemClickListener.onItemClick(news.getId());
                        }
                    }
                }
            });

        }
    }
}
