package com.kha.cbc.comfy.presenter;

import android.util.Log;
import com.avos.avoscloud.*;
import com.kha.cbc.comfy.model.Stage;
import com.kha.cbc.comfy.model.TeamCard;
import com.kha.cbc.comfy.view.common.BaseRefreshView;
import com.kha.cbc.comfy.view.team.StageFragView;
import com.kha.cbc.comfy.view.team.StageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public class TeamDetailPresenter extends BasePresenter {

    BaseRefreshView view;

    public TeamDetailPresenter(BaseRefreshView view) {
        this.view = view;
    }

    public void loadAllStages(String objectId, List<Stage> stageList) {
        view.refresh(true);
        AVObject task = AVObject.createWithoutData("TeamTask", objectId);
        AVQuery<AVObject> queryForStage = new AVQuery<>("Stage");
        queryForStage.whereEqualTo("TeamTask", task);
        queryForStage.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> alist, AVException e) {
                if (alist == null)
                    return;
                if (alist.isEmpty()) {
                    view.onComplete();
                    view.refresh(false);
                }
                for (AVObject stage : alist) {
                    AVQuery<AVObject> queryForCard = new AVQuery<>("TeamCard");
                    queryForCard.include("Executor");
                    queryForCard.whereEqualTo("Stage", stage);
                    queryForCard.orderByAscending("createdAt");
                    queryForCard.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            ArrayList<TeamCard> tempCardList = new ArrayList<>();
                            for (AVObject card : list) {
                                tempCardList.add(new TeamCard(stage.getString("TaskId"),
                                        card.getAVObject("Executor").getString("username"),
                                        card.getString("CardTitle"),
                                        card.getObjectId(),
                                        card.getString("Description")));
                            }
                            stageList.add(new Stage(tempCardList,
                                    stage.getString("Title"),
                                    stage.getObjectId(),
                                    stage.getInt("Index")));
                            if (stageList.size() == alist.size()) {
                                view.onComplete();
                                view.refresh(false);
                            }
                        }
                    });
                }
            }
        });
    }

    public void editTask(String taskObjectId, String newTitle) {
        AVObject stage = AVObject.createWithoutData("TeamTask", taskObjectId);
        stage.put("TaskTitle", newTitle);
        stage.saveInBackground();
    }
}
