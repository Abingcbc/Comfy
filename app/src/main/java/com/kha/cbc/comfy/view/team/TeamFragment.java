package com.kha.cbc.comfy.view.team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.TeamTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/19
 */
public class TeamFragment extends Fragment implements TeamFragView {

    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.team_fragment, container, false);
        this.swipeRefreshLayout = view.findViewById(R.id.team_swipe_fresh);
        this.recyclerView = view.findViewById(R.id.team_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<TeamTask> list = new ArrayList<>();
        list.add(new TeamTask("1", "1", "1"));
        list.add(new TeamTask("2", "2", "2"));
        recyclerView.setAdapter(new TeamTaskAdapter(list));
        return view;
    }

    public static TeamFragment getInstance() {
        TeamFragment fragment = new TeamFragment();
        return fragment;
    }
}
