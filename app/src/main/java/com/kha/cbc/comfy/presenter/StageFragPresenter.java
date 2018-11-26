package com.kha.cbc.comfy.presenter;

import com.avos.avoscloud.AVObject;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public class StageFragPresenter extends BasePresenter {

    public void postStage(String objectId, String title) {
        AVObject teamTask = AVObject.createWithoutData("TeamTask", objectId);
        AVObject stage = AVObject.create("Stage");
        stage.put("TeamTask", teamTask);
        stage.put("Title", title);
        stage.saveInBackground();
    }
}
