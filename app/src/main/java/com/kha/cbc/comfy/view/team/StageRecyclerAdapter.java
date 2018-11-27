package com.kha.cbc.comfy.view.team;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.TeamCard;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/27
 */
public class StageRecyclerAdapter extends RecyclerView.Adapter<StageRecyclerAdapter.ViewHolder> {

    List<TeamCard> teamCardList;

    StageRecyclerAdapter(List<TeamCard> teamCardList) {
        this.teamCardList = teamCardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleView.setText(teamCardList.get(position).getTitle());
        holder.descriptionView.setText(teamCardList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return teamCardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        TextView descriptionView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.team_card_title);
            descriptionView = itemView.findViewById(R.id.team_card_description);
        }
    }
}
