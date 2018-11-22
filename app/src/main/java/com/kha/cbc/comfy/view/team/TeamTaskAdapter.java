package com.kha.cbc.comfy.view.team;

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

    TeamTaskAdapter(List<TeamTask> teamTaskList) {
        this.teamTaskList = teamTaskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.team_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TeamTask teamTask = teamTaskList.get(position);
        holder.titleView.setText(teamTask.getTitle());
        //holder.circleImageView.setImageURI(teamTask.getImageUrl);
    }

    @Override
    public int getItemCount() {
        return teamTaskList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        CircleImageView circleImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.team_task_title);
            circleImageView = itemView.findViewById(R.id.team_task_circle);
        }
    }
}
