package com.kha.cbc.comfy.presenter;

import com.kha.cbc.comfy.entity.GDPersonalTask;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.model.PersonalTask;
import com.kha.cbc.comfy.view.personal.PersonalFragView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.LinkedList;
import java.util.List;

public class PersonalFragPresenter extends BasePresenter {

    PersonalFragView personalFragView;

    public PersonalFragPresenter(PersonalFragView view) {
        personalFragView = view;
    }


    public List loadAllTasksFromDB(GDPersonalTaskDao taskDao) {
        getSubscriptions().add(Observable.just("load")
                .map(s -> {
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
                .subscribe(personalFragView::onLoadAllFromDBSuccess,
                        personalFragView::onLoadAllFromDBError));
        return null;
    }
}
