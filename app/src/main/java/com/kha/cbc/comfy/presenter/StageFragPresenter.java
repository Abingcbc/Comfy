package com.kha.cbc.comfy.presenter;

import com.avos.avoscloud.*;
import com.kha.cbc.comfy.model.TeamCard;
import com.kha.cbc.comfy.presenter.Notification.CloudPushHelper;
import com.kha.cbc.comfy.view.common.BaseRefreshView;
import com.kha.cbc.comfy.view.team.StageFragView;
import com.kha.cbc.comfy.view.team.StageFragment;
import com.kha.cbc.comfy.view.team.StageRecyclerAdapter;
import com.kha.cbc.comfy.view.team.StageRecyclerView;

import java.util.List;

public class StageFragPresenter extends BasePresenter {

    BaseRefreshView view;

    public StageFragPresenter(BaseRefreshView view) {
        this.view = view;
    }

    public void postStage(String objectId, String title, int index) {
        AVObject teamTask = AVObject.createWithoutData("TeamTask", objectId);
        AVObject stage = AVObject.create("Stage");
        stage.put("TeamTask", teamTask);
        stage.put("Title", title);
        stage.put("Index", index);
        stage.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                view.refresh(true);
            }
        });
    }

    public void completeCard(String cardObjectId, String taskObjectId, String cardTitle) {
        AVQuery<AVObject> query = new AVQuery<>("TeamCard");
        query.getInBackground(cardObjectId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e != null) {
                    e.printStackTrace();
                }
                avObject.deleteInBackground();
                CloudPushHelper.pushOperationOnCard(taskObjectId, cardTitle, true);
            }
        });
    }

    public void deleteCard(String cardObjectId, String taskObjectId, String cardTitle) {
        AVQuery<AVObject> query = new AVQuery<>("TeamCard");
        query.getInBackground(cardObjectId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e != null) {
                    e.printStackTrace();
                }
                avObject.deleteInBackground();
                CloudPushHelper.pushOperationOnCard(taskObjectId, cardTitle, false);
            }
        });
    }

    public void editStage(String stageObjectId, String stageTitle) {
        AVObject stage = AVObject.createWithoutData("Stage", stageObjectId);
        stage.put("Title", stageTitle);
        stage.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                ((StageFragView) (StageFragment) view).reload();
            }
        });
    }

    public void deleteStage(String stageObjectId) {
        AVObject stage = AVObject.createWithoutData("Stage", stageObjectId);
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
                stage.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        ((StageFragment) view).reload();
                    }
                });
            }
        });
    }

    public void getCardImageUrl(StageRecyclerView view,
                                StageRecyclerAdapter.ViewHolderStage holder,
                                TeamCard card) {
        AVObject object = AVObject.createWithoutData("TeamCard", card.getObjectId());
        object.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                avObject.getAVObject("Executor").fetchInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        String imageUrl = avObject.getString("avatarUrl");
                        view.onLoadImageCompleted(imageUrl, holder);
                    }
                });
            }
        });
    }
}
