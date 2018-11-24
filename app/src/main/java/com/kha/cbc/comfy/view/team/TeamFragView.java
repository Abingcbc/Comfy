package com.kha.cbc.comfy.view.team;

import com.kha.cbc.comfy.model.TeamTask;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/19
 */
public interface TeamFragView {

    void onGetAllTaskSuccess(List<TeamTask> teamTasks);
    void onGetAllTaskError(Throwable e);
    void setRefreshing(boolean b);
}
