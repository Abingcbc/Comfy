package com.kha.cbc.comfy.presenter

import com.avos.avoscloud.*
import com.kha.cbc.comfy.data.applySchedulers
import com.kha.cbc.comfy.data.plusAssign
import com.kha.cbc.comfy.data.subscribeBy
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.view.login.LoginView

class LoginPresenter(override val view: LoginView) : LeanCloudPresenter(view) {
    fun onRegister(user: String, password: String) {
        subscriptions += repository.registerNewUser(user, password)
            .applySchedulers()
            .subscribeBy(
                onSuccess = ::uploadComfyUser,
                onError = view::onRegisterError
            )
    }

    fun uploadComfyUser(user: User){
        val comfyUser = AVObject("ComfyUser")
        comfyUser.put("username", user.username)
        comfyUser.saveInBackground(object: SaveCallback(){
            override fun done(p0: AVException?) {
                requeryComfyUser(user.username!!, user)
            }
        })
    }

    fun requeryComfyUser(username: String, user: User){
        val query = AVQuery<AVObject>("ComfyUser")
        query.whereEqualTo("username", username)
        query.findInBackground(object: FindCallback<AVObject>(){
            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                val queryUesr = p0!![0]
                user.comfyUserObjectId = queryUesr.objectId
                view.onRegisterComplete(user)
            }
        })
    }

    fun onLogin(user: String, password: String){
        subscriptions += repository.login(user, password)
            .applySchedulers()
            .subscribeBy(
                onSuccess = view::onLoginComplete,
                onError = view::onLoginError
            )
    }
}