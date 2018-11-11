package com.kha.cbc.comfy.view.main;

import com.kha.cbc.comfy.common.BasePresenter;
import com.kha.cbc.comfy.data.entity.GDPersonalTask;
import com.kha.cbc.comfy.data.model.PersonalTask;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/6
 */

public class MainPresenter extends BasePresenter {

    MainView mainView;

    MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    List loadAllTasksFromDB(GDPersonalTaskDao taskDao) {
        subscriptions.add(Observable.just("load")
                .map(s -> {
                    //TODO:可在此添加网路的检测及同步
                    List<GDPersonalTask> gdPersonalTasks = taskDao.loadAll();
                    return gdPersonalTasks;
                }).map(gdPersonalTasks -> {
                    List<PersonalTask> taskList = new LinkedList<>();
                    for (GDPersonalTask task : gdPersonalTasks) {
                        taskList.add(new PersonalTask(task));
                    }
                    return taskList;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mainView::OnLoadAllFromDBSuccess));
        return null;
    }

}
