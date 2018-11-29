package com.kha.cbc.comfy.presenter

import com.kha.cbc.comfy.data.applySchedulers
import com.kha.cbc.comfy.data.plusAssign
import com.kha.cbc.comfy.data.subscribeBy
import com.kha.cbc.comfy.view.main.MainView

class MainPresenter(override val view: MainView) : LeanCloudPresenter(view) {
}