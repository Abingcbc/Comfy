package com.kha.cbc.comfy.presenter

import com.kha.cbc.comfy.data.applySchedulers
import com.kha.cbc.comfy.data.plusAssign
import com.kha.cbc.comfy.data.subscribeBy
import com.kha.cbc.comfy.view.login.LoginView

class LoginPresenter(override val view: LoginView): LeanCloudPresenter(view){
    fun onRegister(user: String, password: String){
        subscriptions += repository.registerNewUser(user, password)
            .applySchedulers()
            .subscribeBy (
                onSuccess = view::onRegisterComplete,
                onError = view::onRegisterError
            )
    }

    fun onLogin(user: String, password: String){
        subscriptions += repository.login(user, password)
            .applySchedulers()
            .subscribeBy (
                onSuccess = view::onLoginComplete,
                onError = view::onLoginError
            )
    }
}