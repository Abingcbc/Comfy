package com.kha.cbc.comfy.presenter

import com.avos.avoscloud.*
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.view.common.UserServiceView
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.LogInCallback



class UserServicePresenter(val view: UserServiceView){
    fun usernameChange(username: String, password: String){
        val oldUsername = User.username
        AVUser.logInInBackground(oldUsername, password, object : LogInCallback<AVUser>() {
            override fun done(p0: AVUser?, p1: AVException?) {
                val query = AVQuery<AVUser>("_User")
                query.whereEqualTo("username", username)
                query.findInBackground(object: FindCallback<AVUser>(){
                    override fun done(listUser: MutableList<AVUser>?, p1: AVException?) {
                        if(listUser!!.size == 0){
                            p0!!.put("username", username)
                            p0.saveInBackground(object: SaveCallback(){
                                override fun done(e: AVException?) {
                                    User.username = username
                                    User.sessionToken = p0.sessionToken
                                    val query = AVQuery<AVObject>("ComfyUser")
                                    query.whereEqualTo("username", oldUsername)
                                    query.findInBackground(object : FindCallback<AVObject>() {
                                        override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                                            val oldComfyUser = p0!![0]
                                            oldComfyUser.put("username", username)
                                            oldComfyUser.saveInBackground(object : SaveCallback(){
                                                override fun done(p0: AVException?) {
                                                    view.usernameChangeFinished()
                                                }
                                            })
                                        }
                                    })
                                }
                            })
                        }
                        else{
                            view.usernameChangeFailed()
                        }
                    }
                })
            }
        })

    }

    fun passwordChange(password: String){
        AVUser.logInInBackground(User.username, password, object : LogInCallback<AVUser>() {
            override fun done(avUser: AVUser?, p1: AVException?) {
                avUser!!.put("password", password)
                avUser.saveInBackground(object: SaveCallback(){
                    override fun done(e: AVException?) {
                        User.username = avUser.username
                        User.sessionToken = avUser.sessionToken
                        view.passwordChangeFinished()
                    }
                })
            }
        })
    }
}