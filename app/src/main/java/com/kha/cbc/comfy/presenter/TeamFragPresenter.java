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
                teamTaskList.add(new TeamTask("0", "0","0"));
                for (AVObject teamTask : list) {
                    teamTaskList.add(new TeamTask(
                            teamTask.getString("TaskTitle"),
                            teamTask.getString("CreateUserName"),
                            teamTask.getObjectId()));
                }
                AVQuery<AVObject> queryP = new AVQuery<>("UserTaskMap");
                AVObject user = AVObject.createWithoutData("ComfyUser", User.INSTANCE.getComfyUserObjectId());
                queryP.whereEqualTo("Member", user);
                queryP.include("TeamTask");
                queryP.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e != null) {
                            e.printStackTrace();
                            return;
                        }
                        TeamFragment.numOfCreate = teamTaskList.size();
                        teamTaskList.add(new TeamTask("0", "0","0"));
                        for (AVObject map : list) {
                            AVObject teamTask = map.getAVObject("TeamTask");
                            if (!teamTask.getString("CreateUserName").
                                    equals(User.INSTANCE.getUsername())) {
                                teamTaskList.add(new TeamTask(
                                        teamTask.getString("TaskTitle"),
                                        teamTask.getString("CreateUserName"),
                                        teamTask.getObjectId()));
                            }
                        }
                        view.onComplete();
                        view.refresh(false);
                    }
                });
            }
        });
    }


    public void deleteTask(String taskObjectId) {
        view.refresh(true);
        AVObject task = AVObject.createWithoutData("TeamTask", taskObjectId);
        AVQuery<AVObject> queryStage = new AVQuery<>("Stage");
        queryStage.whereEqualTo("TeamTask", task);
        queryStage.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject stage: list) {
                    AVQuery<AVObject> queryCard = new AVQuery<>("TeamCard");
                    queryCard.whereEqualTo("Stage", stage);
                    queryCard.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            AVObject.deleteAllInBackground(list, new DeleteCallback() {
                                @Override
                                public void done(AVException e) {

                                }
                            });
                            stage.deleteInBackground();
                        }
                    });
                }
                task.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        ((TeamFragment) view).reload();
                    }
                });
            }
        });
        AVQuery<AVObject> queryMap = new AVQuery<>("UserTaskMap");
        queryMap.whereEqualTo("TeamTask", task);
        queryMap.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                AVObject.deleteAllInBackground(list, new DeleteCallback() {
                    @Override
                    public void done(AVException e) {

                    }
                });
            }
        });
    }

}
