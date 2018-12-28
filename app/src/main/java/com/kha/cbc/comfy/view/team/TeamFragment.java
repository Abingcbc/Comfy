package com.kha.cbc.comfy.view.team;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.TeamTask;
import com.kha.cbc.comfy.presenter.TeamFragPresenter;
import com.kha.cbc.comfy.view.common.BaseRefreshView;
import com.kha.cbc.comfy.view.plus.PlusTaskActivity;

import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by ABINGCBC
 * on 2018/11/19
 */
//user:khazeus@outlook.com
//password: A12345678
public class TeamFragment extends Fragment
        implements TeamFragView, BaseRefreshView {

    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    List<TeamTask> teamTaskList;
    TeamFragPresenter presenter = new TeamFragPresenter(this);
    public static int numOfCreate;

    @Override
    public void onChatReady(String conversationId) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.team_fragment, container, false);
        this.swipeRefreshLayout = view.findViewById(R.id.team_swipe_fresh);
        this.recyclerView = view.findViewById(R.id.team_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new TeamItemDecoration());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
        reload();
        return view;
    }

    public static TeamFragment getInstance() {
        TeamFragment fragment = new TeamFragment();
        return fragment;
    }

    public void plusTask() {
        Intent intent = new Intent(getContext(), PlusTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    public void reload() {
        teamTaskList = new LinkedList<>();
        presenter.getAllCreateTask(teamTaskList);
    }

    void goToDetail(String title, String objectId) {
        Intent intent = new Intent(this.getContext(), TeamDetailActivity.class);
        intent.putExtra("taskTitle", title);
        intent.putExtra("taskObjectId", objectId);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK)
                    reload();
            case 2:
                if (resultCode == RESULT_CANCELED) {
                    presenter.deleteTask(data.getStringExtra("taskObjectId"));
                }
        }
    }

    @Override
    public void refresh(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
    }

    @Override
    public void onComplete() {
        recyclerView.setAdapter(new TeamTaskAdapter(teamTaskList, this,
                numOfCreate, getContext()));
    }

    class TeamItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.top = 20;
            outRect.left = 35;
            outRect.right = 35;
        }
    }
}
