package com.kha.cbc.comfy.view.common

import androidx.appcompat.app.AppCompatActivity
import com.kha.cbc.comfy.presenter.Presenter

abstract class BaseActivityWithPresenter : AppCompatActivity(){

    abstract val presenter: Presenter

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

}