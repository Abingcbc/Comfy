package com.kha.cbc.comfy.presenter

import com.kha.cbc.comfy.data.LeanCloudRepositoryImpl
import com.kha.cbc.comfy.view.common.BaseView

abstract class LeanCloudPresenter(
    open val view: BaseView
) : BasePresenter() {
    val repository by lazy { LeanCloudRepositoryImpl() }
}