package com.kha.cbc.comfy.presenter;

import android.content.Context;
import com.avos.avoscloud.*;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.model.TeamTask;
import com.kha.cbc.comfy.model.User;
import com.kha.cbc.comfy.view.main.MainActivity;
import com.kha.cbc.comfy.view.plus.PlusTaskView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ABINGCBC
 * on 2018/11/5
 */
public class PlusTaskPresenter extends BasePresenter {

    PlusTaskView view;

    public PlusTaskPresenter(PlusTaskView plusTaskView) {
        view = plusTaskView;
    }

    public void postTask(TeamTask teamTask, Context context) {
        String title = teamTask.getTitle();
        String taskId = teamTask.getId();
        AVObject task = new AVObject("TeamTask");
        task.put("CreateUserName", User.INSTANCE.getUsername());
        task.put("TaskTitle", title);
        task.put("TaskId", taskId);
        task.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVObject map = new AVObject("UserTaskMap");
                AVObject teamTask = AVObject.createWithoutData("TeamTask",
                        task.getObjectId());
                AVObject user = AVObject.createWithoutData("ComfyUser",
                        User.INSTANCE.getComfyUserObjectId());
                map.put("TeamTask", teamTask);
                map.put("Member", user);
                map.saveInBackground();
            }
        });
    }
}
