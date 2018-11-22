package com.kha.cbc.comfy.view.main;

import com.kha.cbc.comfy.common.BasePresenter;
import com.kha.cbc.comfy.data.entity.GDPersonalTask;
import com.kha.cbc.comfy.data.model.PersonalTask;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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



}
