package com.kha.cbc.comfy.view.login

import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.view.common.BaseView

interface LoginView : BaseView {
    fun onRegisterComplete(user: User)
    fun onRegisterError(error: Throwable)
    fun onLoginComplete(user: User)
    fun onLoginError(error: Throwable)
}