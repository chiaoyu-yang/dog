package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


public class BookActivity extends RecyclerView.Adapter<BookActivity.myviewholder>
{
    BookModel data[];

    public BookActivity(BookModel[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.books,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.t1.setText(data[position].getName());
        Glide.with(holder.t1.getContext()).load("http://140.131.114.145/Android/112_dog/books/"+data[position].getImage()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView t1;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);

            img=itemView.findViewById(R.id.booklist_image_button);
            t1=itemView.findViewById(R.id.booklist_text);
        }
    }

}

