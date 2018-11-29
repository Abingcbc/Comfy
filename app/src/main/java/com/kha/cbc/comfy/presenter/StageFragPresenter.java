package com.kha.cbc.comfy.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
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
}
