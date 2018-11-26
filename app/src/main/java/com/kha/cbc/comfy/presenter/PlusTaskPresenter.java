package com.kha.cbc.comfy.presenter;

import com.avos.avoscloud.AVObject;
import com.kha.cbc.comfy.model.TeamTask;
import com.kha.cbc.comfy.model.User;
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

    public void postTask(TeamTask teamTask) {
        String title = teamTask.getTitle();
        String taskId = teamTask.getId();

        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                AVObject teamTask = new AVObject("TeamTask");
                teamTask.put("CreateUserName", User.INSTANCE.getUsername());
                teamTask.put("TaskTitle", title);
                teamTask.put("TaskId", taskId);
                teamTask.saveInBackground();
            }
        });
        getSubscriptions().add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }
}
