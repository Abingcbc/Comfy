package com.kha.cbc.comfy.presenter

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter : Presenter {

    var subscriptions = CompositeDisposable()

    override fun onViewDestroyed() {
        subscriptions.dispose()
    }
}