package com.example.soulgo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<RankItem> rankList;

    public RankAdapter(List<RankItem> rankList) {
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
        RankItem rankItem = rankList.get(position);
        holder.txtDivision.setText(String.valueOf(rankItem.getDivision()));
        holder.txtUsername.setText(rankItem.getNickname());
        holder.txtIntegral.setText(String.valueOf(rankItem.getPoints()));
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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



