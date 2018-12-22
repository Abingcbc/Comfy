package com.kha.cbc.comfy.presenter;

import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.avos.avoscloud.*;
import com.google.android.material.snackbar.Snackbar;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.model.TeamTask;
import com.kha.cbc.comfy.model.User;
import com.kha.cbc.comfy.view.common.AvatarView;
import com.kha.cbc.comfy.view.common.BaseRefreshView;
import com.kha.cbc.comfy.view.team.TeamFragment;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


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

}
