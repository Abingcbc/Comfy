package com.kha.cbc.comfy.view.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.TeamTask;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/12/21
 */
public class TeamTaskMemberAdapter extends RecyclerView.Adapter<TeamTaskMemberAdapter.ViewHolder> {

    private TeamTask teamTask;
    private List<String> memberImageUrls;
    private static final int LIMIT = 4;
    private Context context;

    TeamTaskMemberAdapter(TeamTask teamTask) {
        this.teamTask = teamTask;
        this.memberImageUrls = teamTask.getMemberImageUrls();
    }

    @NonNull
    @Override
    public TeamTaskMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.portrait, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamTaskMemberAdapter.ViewHolder holder, int position) {
//        if (getItemCount() == LIMIT && position == LIMIT-1) {
//            holder.memberView.setBackgroundResource(R.drawable.dots_horizontal);
//        } else {
//            Glide.with(context).load(memberImageUrls.get(position)).into(holder.memberView);
//        }
    }

    @Override
    public int getItemCount() {
//        if (memberImageUrls.size() < LIMIT)
//            return memberImageUrls.size();
//        else
            return LIMIT;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView memberView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memberView = itemView.findViewById(R.id.team_task_member);
        }

    }
}
