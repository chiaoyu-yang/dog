package com.example.soulgo.News;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.soulgo.Constants;
import com.example.soulgo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<PostModel> data;
    private Context context;

    private boolean isLiked;

    public PostAdapter(Context context, List<PostModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PostModel post = data.get(position);
        holder.message.setText(post.getMessage());
        holder.messageLike.setText(post.getMessageLike());
        holder.messageName.setText(post.getNickname());

        post.setLiked(getLikeStatus(post.getMessageId()));

        if (post.isLiked()) {
            holder.messageLikeBtn.setImageResource(R.drawable.active_like);
        } else {
            holder.messageLikeBtn.setImageResource(R.drawable.like_button);
        }

        holder.messageLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.setLiked(!post.isLiked());

                String action = post.isLiked() ? "like" : "dislike";

                if (post.isLiked()) {
                    holder.messageLikeBtn.setImageResource(R.drawable.active_like);
                } else {
                    holder.messageLikeBtn.setImageResource(R.drawable.like_button);
                }

                updateLike(holder, post.getMessageId(), action);
                saveLikeStatus(post.getMessageId(), post.isLiked());
            }
        });
    }

    private void updateLike(MyViewHolder holder, String messageId, String action) {
        String url = Constants.URL_UpdateMessageLike;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean("error");
                            String message = jsonResponse.getString("message");
                            String like = jsonResponse.getString("like");
                            holder.messageLike.setText(like);

                            if (!error) {
                                Toast.makeText(context.getApplicationContext(), "Success: " + message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context.getApplicationContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context.getApplicationContext(), "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", messageId); // 修改为合适的参数名
                params.put("action", action);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void saveLikeStatus(String messageId, boolean isLiked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(messageId, isLiked);
        editor.apply();
    }

    private boolean getLikeStatus(String messageId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean(messageId, false);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message, messageLike, messageName;
        ImageButton messageLikeBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            messageLike = itemView.findViewById(R.id.messageLike);
            messageName = itemView.findViewById(R.id.messageName);
            messageLikeBtn = itemView.findViewById(R.id.messageLikeBtn);
        }
    }
}
