package com.kha.cbc.comfy.presenter;

import com.avos.avoscloud.*;
import com.kha.cbc.comfy.presenter.Notification.CloudPushHelper;
import com.kha.cbc.comfy.view.common.BaseRefreshView;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
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
                CloudPushHelper.pushOperation(taskObjectId, cardTitle, true);
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
                CloudPushHelper.pushOperation(taskObjectId, cardTitle, false);
            }
        });
    }
}
