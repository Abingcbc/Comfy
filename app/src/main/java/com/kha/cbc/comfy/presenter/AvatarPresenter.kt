package com.kha.cbc.comfy.presenter

import android.net.Uri
import com.avos.avoscloud.*
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.view.common.AvatarView


class AvatarPresenter(override val view: AvatarView): LeanCloudPresenter(view){
    //TODO: Structure not perfect

    fun loadAvatar(){



//        if(avatarUrl == ""){
            val query = AVQuery<AVObject>("ComfyUser")
            query.whereEqualTo("username", User.username)
            query.findInBackground(object: FindCallback<AVObject>(){
                override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                    val userObject = p0!![0]
                    val avatarQuery = AVQuery<AVObject>("ComfyUserInfoMap")
                    avatarQuery.whereEqualTo("user", userObject)
                    avatarQuery.include("avatar")
                    avatarQuery.findInBackground(object: FindCallback<AVObject>(){
                        override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                            val avatar = p0!![0].getAVFile<AVFile>("avatar")
                            view.downloadAvatarFinish(avatar.url)
                        }
                    })
                }
            })
//        }
//        else{
//            view.downloadAvatarFinish(avatarUrl)
//        }
    }

    fun uploadAvatar(uri: Uri){
        view.setProgressBarVisible()

        val query = AVQuery<AVObject>("ComfyUser")
        query.whereEqualTo("username", User.username)
        query.findInBackground(object: FindCallback<AVObject>(){
            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                val userObject = p0!![0]
                val avatarQuery = AVQuery<AVObject>("ComfyUserInfoMap")
                avatarQuery.whereEqualTo("user", userObject).include("avatar")
                avatarQuery.findInBackground(object: FindCallback<AVObject>(){
                    override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                        if(p0 != null && p0.isNotEmpty()){
                            val prevMap = p0[0]
                            val prevAvatar = prevMap.getAVFile<AVFile>("avatar")
                            prevAvatar.deleteInBackground()
                            prevMap.deleteInBackground()
                        }
                        val sourceFile = uri.path
                        val uploadFile = AVFile.withAbsoluteLocalPath(User.username + "avatar.jpg"
                            , sourceFile)
                        uploadFile.saveInBackground(object: SaveCallback(){
                            override fun done(p0: AVException?) {
                                val mapObject = AVObject("ComfyUserInfoMap")
                                mapObject.put("user", userObject)
                                mapObject.put("avatar", uploadFile)
                                mapObject.saveInBackground(object :SaveCallback(){
                                    override fun done(p0: AVException?) {
                                        view.uploadAvatarFinish(uploadFile.url)
                                    }
                                })
                            }
                        },object: ProgressCallback(){
                            override fun done(p0: Int?) {
                                view.uploadProgressUpdate(p0)
                            }
                        })
                    }
                })
            }
        })

    }
}