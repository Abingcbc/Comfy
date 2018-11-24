package com.kha.cbc.comfy.presenter;

import com.avos.avoscloud.*;
import com.kha.cbc.comfy.model.Stage;
import com.kha.cbc.comfy.model.TeamCard;
import com.kha.cbc.comfy.view.team.TeamDetailView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public class TeamDetailPresenter extends BasePresenter {

    TeamDetailView view;

    public TeamDetailPresenter(TeamDetailView view) {
        this.view = view;
    }

    public List<Stage> loadAllStages(String objectId) {
        List<Stage> answer = new ArrayList<>();
        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            HashMap<String, TeamCard> stageToCardMap = new HashMap<>();
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                AVObject task = AVObject.createWithoutData("TeamTask", objectId);
                AVQuery<AVObject> queryForStage = new AVQuery("Stage");
                queryForStage.whereEqualTo("TeamTask", task);
                queryForStage.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        for (AVObject stage : list) {
                            answer.add(new Stage(new ArrayList<>()));
                            AVQuery<AVObject> queryForCard = new AVQuery<>("TeamCard");
                            queryForCard.whereEqualTo("Stage", stage);
                            queryForCard.findInBackground(new FindCallback<AVObject>() {
                                @Override
                                public void done(List<AVObject> list, AVException e) {
                                    for (AVObject card : list) {
                                        tempCardList.add(new TeamCard(stage.getString("TaskId"),
                                                card.getString("Executor"),
                                                card.getString("CardTitle")));
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        return null;
    }
}
