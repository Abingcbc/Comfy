package com.kha.cbc.comfy.view.team;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.TeamTask;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/19
 */
public class TeamTaskAdapter extends RecyclerView.Adapter<TeamTaskAdapter.ViewHolder> {

    List<TeamTask> teamTaskList;
    TeamFragment fragment;
    int numOfCreate;

    TeamTaskAdapter(List<TeamTask> teamTaskList,TeamFragment fragment, int numOfCreate) {
        this.teamTaskList = teamTaskList;
        this.fragment = fragment;
        this.numOfCreate = numOfCreate;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else if (position == numOfCreate)
            return 1;
        else
            return 2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.info_create, parent, false);
                ViewHolder viewHolder1 = new ViewHolder(view1);
                return viewHolder1;
            case 1:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.info_participate, parent, false);
                ViewHolder viewHolder2 = new ViewHolder(view2);
                return viewHolder2;
                default:
                    View view = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.team_task, parent, false);
                    ViewHolder viewHolder = new ViewHolder(view);
                    return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position != 0 && position != numOfCreate) {
            TeamTask teamTask = teamTaskList.get(position);
            holder.titleView.setText(teamTask.getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.goToDetail(teamTask.getTitle(), teamTask.getObjectId());
                }
            });
            //holder.circleImageView.setImageURI(teamTask.getImageUrl);
        }
    }

    @Override
    public int getItemCount() {
        return teamTaskList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        CircleImageView circleImageView;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            titleView = itemView.findViewById(R.id.team_task_title);
            circleImageView = itemView.findViewById(R.id.team_task_circle);
        }
    }
}
