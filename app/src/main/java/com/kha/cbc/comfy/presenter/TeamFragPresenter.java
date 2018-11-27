package com.kha.cbc.comfy.presenter;

import com.avos.avoscloud.*;
import com.kha.cbc.comfy.model.TeamTask;
import com.kha.cbc.comfy.model.User;
import com.kha.cbc.comfy.view.common.BaseRefreshView;
import com.kha.cbc.comfy.view.team.TeamFragment;

import java.util.List;


/**
 * Created by ABINGCBC
 * on 2018/11/19
 */
public class TeamFragPresenter extends BasePresenter {

    BaseRefreshView view;

    public TeamFragPresenter(BaseRefreshView view) {
        this.view = view;
    }

    public void getAllCreateTask(List<TeamTask> teamTaskList) {
        view.refresh(true);
        AVQuery<AVObject> query = new AVQuery<>("TeamTask");
        query.whereEqualTo("CreateUserName", User.INSTANCE.getUsername());
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                teamTaskList.add(new TeamTask("0", "0", "0", "0"));
                for (AVObject teamTask : list) {
                    teamTaskList.add(new TeamTask(
                            teamTask.getString("TaskTitle"),
                            teamTask.getString("CreateUserName"),
                            //TODO:可自选项目图片
                            null,
                            teamTask.getObjectId()));
                }
                AVQuery<AVObject> queryP = new AVQuery<>("UserTaskMap");
                queryP.whereEqualTo("ParticipateUser", AVUser.getCurrentUser());
                queryP.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        TeamFragment.numOfCreate = teamTaskList.size();
                        teamTaskList.add(new TeamTask("0", "0", "0", "0"));
                        for (AVObject map : list) {
                            AVObject teamTask = map.getAVObject("TeamTask");
                            teamTaskList.add(new TeamTask(
                                    teamTask.getString("TaskTitle"),
                                    teamTask.getString("CreateUserName"),
                                    //TODO:可自选项目图片
                                    null,
                                    teamTask.getObjectId()));
                        }
                        view.onComplete();
                        view.refresh(false);
                    }
                });
            }
        });
    }

}
