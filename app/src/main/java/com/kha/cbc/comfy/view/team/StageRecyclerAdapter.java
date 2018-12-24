package com.kha.cbc.comfy.view.team;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.Stage;
import com.kha.cbc.comfy.model.TeamCard;
import com.kha.cbc.comfy.view.plus.PlusCardActivity;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/27
 */
public class StageRecyclerAdapter extends RecyclerView.Adapter<StageRecyclerAdapter.ViewHolder> {

    List<TeamCard> teamCardList;
    StageFragment fragment;
    String taskObjectId;

    StageRecyclerAdapter(List<TeamCard> teamCardList, StageFragment fragment, String taskObjectId) {
        this.teamCardList = teamCardList;
        this.fragment = fragment;
        this.taskObjectId = taskObjectId;
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
        holder.changeView.setOnClickListener(v -> {
            Intent intent = new Intent(fragment.getContext(), PlusCardActivity.class);
            intent.putExtra("type", 3);
            intent.putExtra("taskObjectId", taskObjectId);
            intent.putExtra("cardObjectId", teamCardList.get(position).getObjectId());
            fragment.getContext().startActivity(intent);
            notifyItemChanged(position);
        });
        holder.checkView.setOnClickListener(v -> {
            fragment.getPresenter().completeCard(teamCardList.get(position).getObjectId(),
                    taskObjectId,
                    teamCardList.get(position).getTitle());
            teamCardList.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(0, getItemCount());
        });
        holder.deleteView.setOnClickListener(v -> {
            fragment.getPresenter().deleteCard(teamCardList.get(position).getObjectId(),
                    taskObjectId,
                    teamCardList.get(position).getTitle());
            teamCardList.remove(position);
            notifyItemRemoved(position);
            notifyItemChanged(0, getItemCount());
        });
    }

    @Override
    public int getItemCount() {
        return teamCardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        TextView descriptionView;
        ImageView changeView;
        ImageView checkView;
        ImageView deleteView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.team_card_title);
            descriptionView = itemView.findViewById(R.id.team_card_description);
            changeView = itemView.findViewById(R.id.team_card_change);
            checkView = itemView.findViewById(R.id.team_card_check);
            deleteView = itemView.findViewById(R.id.team_card_delete);
        }
    }
}
