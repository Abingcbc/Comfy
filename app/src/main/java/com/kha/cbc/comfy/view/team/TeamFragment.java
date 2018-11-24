package com.kha.cbc.comfy.view.team;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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
import com.kha.cbc.comfy.view.plus.PlusTaskActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ABINGCBC
 * on 2018/11/19
 */
//user:khazeus@outlook.com
//password: A12345678
public class TeamFragment extends Fragment implements TeamFragView {

    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    List<TeamTask> teamTaskList;
    TeamFragPresenter presenter = new TeamFragPresenter(this);
    public static int numOfCreate;

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

    void reload() {
        teamTaskList = new LinkedList<>();
        presenter.getAllCreateTask(teamTaskList);
    }

    void goToDetail(String title, String objectId) {
        Intent intent = new Intent(this.getContext(), TeamDetailActivity.class);
        intent.putExtra("taskTitle", title);
        intent.putExtra("objectId", objectId);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK)
                    reload();
        }
    }

    @Override
    public void onGetAllTaskSuccess(List<TeamTask> teamTasks) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread())
            Log.d("a", "a");
        recyclerView.setAdapter(new TeamTaskAdapter(teamTaskList,this, numOfCreate));
    }

    @Override
    public void onGetAllTaskError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void setRefreshing(boolean b) {
        swipeRefreshLayout.setRefreshing(b);
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
