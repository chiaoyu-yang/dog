package com.example.leaderboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Ranklist> rankList;

    public RecyclerViewAdapter(List<Ranklist> rankList) {
        this.rankList = rankList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ranklist rank = rankList.get(position);
        holder.txtDivision.setText(rank.getDivision());
        holder.txtUsername.setText(rank.getUsername());
        holder.txtIntegral.setText(String.valueOf(rank.getIntegral()));
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDivision;
        TextView txtUsername;
        TextView txtIntegral;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDivision = itemView.findViewById(R.id.txt_division);
            txtUsername = itemView.findViewById(R.id.txt_username);
            txtIntegral = itemView.findViewById(R.id.txt_integral);
        }
    }
}


