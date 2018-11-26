package com.kha.cbc.comfy.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.kha.cbc.comfy.model.Stage;
import com.kha.cbc.comfy.model.TeamCard;
import com.kha.cbc.comfy.view.common.BaseRefreshView;

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
        AVQuery<AVObject> queryForStage = new AVQuery("Stage");
        queryForStage.whereEqualTo("TeamTask", task);
        queryForStage.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> alist, AVException e) {
                if (alist.isEmpty()) {
                    view.onComplete();
                    view.refresh(false);
                }
                for (AVObject stage : alist) {
                    AVQuery<AVObject> queryForCard = new AVQuery<>("TeamCard");
                    queryForCard.whereEqualTo("Stage", stage);
                    queryForCard.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            ArrayList<TeamCard> tempCardList = new ArrayList<>();
                            for (AVObject card : list) {
                                tempCardList.add(new TeamCard(stage.getString("TaskId"),
                                        card.getString("Executor"),
                                        card.getString("CardTitle")));
                            }
                            stageList.add(new Stage(tempCardList, stage.getString("Title")));
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
}
