package com.kha.cbc.comfy.view.team;

import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.Stage;
import com.kha.cbc.comfy.presenter.TeamDetailPresenter;

import java.util.List;

public class TeamDetailActivity extends AppCompatActivity implements TeamDetailView{

    ProgressBar bar;
    ViewPager viewPager;
    TeamDetailPresenter presenter;
    List<StageFragment> fragmentList;
    List<Stage> stageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        bar = findViewById(R.id.loading_progressBar);
        viewPager = findViewById(R.id.stage_viewpager);
        Toolbar toolbar = findViewById(R.id.team_detail_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        TextView taskTitleView = findViewById(R.id.task_detail_name);
        String taskTitle = intent.getStringExtra("taskTitle");
        String objectId = intent.getStringExtra("objectId");
        taskTitleView.setText(taskTitle);

        presenter = new TeamDetailPresenter(this);
        //stageList = presenter.loadAllStages(objectId);

        viewPager.setAdapter(new TeamDetailFragAdapter(getSupportFragmentManager(), fragmentList));

    }


}
