package com.kha.cbc.comfy.common;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by ABINGCBC
 * on 2018/11/5
 */

public class BasePresenter {

    protected CompositeDisposable subscriptions = new CompositeDisposable();

    public void onViewDestroyed() {
        subscriptions.dispose();
    }
}
