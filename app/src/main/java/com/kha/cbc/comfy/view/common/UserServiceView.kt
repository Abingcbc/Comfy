package com.kha.cbc.comfy.view.common

import com.kha.cbc.comfy.presenter.LoginPresenter

interface UserServiceView :BaseView{
    fun usernameChangeFinished()
    fun usernameChangeFailed()
    fun passwordChangeFinished()
}