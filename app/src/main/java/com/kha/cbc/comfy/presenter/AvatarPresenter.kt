package com.kha.cbc.comfy.presenter

import android.net.Uri
import com.avos.avoscloud.*
import com.kha.cbc.comfy.entity.GDAvatar
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.view.common.AvatarView
import java.io.File


class AvatarPresenter(override val view: AvatarView): LeanCloudPresenter(view){
    //TODO: Structure not perfect
    //TODO: Check the cache function
    fun loadAvatar(usernames: MutableList<String>){
        val usernameQueryList = mutableListOf<AVQuery<AVObject>>()
        for(username in usernames){
            val newQuery = AVQuery<AVObject>("ComfyUser")
            newQuery.whereEqualTo("username", username)
            usernameQueryList.add(newQuery)
        }
        val usernameOr = AVQuery.or(usernameQueryList)
        usernameOr.findInBackground(object: FindCallback<AVObject>(){
            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                if(p0 != null && p0.size > 0){
                    val avatarQueryList = mutableListOf<AVQuery<AVObject>>()
                    for(user in p0){
                        val newQuery = AVQuery<AVObject>("ComfyUserInfoMap")
                        newQuery.whereEqualTo("user", user)
                        newQuery.include("avatar")
                        avatarQueryList.add(newQuery)
                    }
                    val avatarOr = AVQuery.or(avatarQueryList)
                    avatarOr.include("avatar")
                    avatarOr.findInBackground(object: FindCallback<AVObject>(){
                        override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                            if(p0 != null && p0.size > 0){
                                val resultList = mutableListOf<Pair<String, String>>()
                                for(avatar in p0){
                                    val avatarFile = avatar.getAVFile<AVFile>("avatar")
                                    val url = avatarFile.url
                                    val fileName = avatarFile.originalName.split(".")[0]
                                        .split("avatar")[0]
                                    val pair = Pair<String, String>(fileName, url)
                                    resultList.add(pair)
                                }
                                view.downloadAvatarFinish(resultList)
                            }
                        }
                    })
                }
            }
        })
    }

    fun loadAvatar(){

        val historyAvatarList = view.avatarDao.loadAll()
        if(historyAvatarList.size > 0 && historyAvatarList[0] != null){
            val historyAvatar = historyAvatarList[0]
            if(User.username == historyAvatar.username){
                view.downloadAvatarFinish(historyAvatar.avatarUrl)
            }
            else{
                view.avatarDao.deleteAll()
                loadAvatar()
            }
        }
        else{
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
                            if(p0 != null && p0.size > 0){
                                val avatar = p0[0].getAVFile<AVFile>("avatar")
                                view.downloadAvatarFinish(avatar.url)
                            }
                        }
                    })
                }
            })
        }
    }

    fun initiateAvatar(file: File){
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
                        val uploadFile = AVFile.withFile(User.username + "avatar.jpg", file)
                        uploadFile.saveInBackground(object: SaveCallback(){
                            override fun done(p0: AVException?) {
                                val mapObject = AVObject("ComfyUserInfoMap")
                                mapObject.put("user", userObject)
                                mapObject.put("avatar", uploadFile)
                                mapObject.saveInBackground(object :SaveCallback(){
                                    override fun done(p0: AVException?) {
                                        view.uploadAvatarFinish(uploadFile.url)
                                        try {
                                            view.avatarDao.deleteAll()
                                            val historyAvatar = GDAvatar(User.username, uploadFile.url)
                                            view.avatarDao.insert(historyAvatar)
                                        }
                                        catch (e: Exception){
                                            e.printStackTrace()
                                        }
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
                        val uploadFile: AVFile
//                        if(sourceFile == "/mipmap/ic_launcher_round"){
//                            val file = File(
//                                URI.create(Uri.Builder().scheme("res").path("R.mipmap.ic_launcher_round")
//                                    .build().toString())
//                            )
//                            uploadFile = AVFile.withFile(User.username + "avatar.jpg", file)
//                        }
//                        else{
                            uploadFile = AVFile.withAbsoluteLocalPath(User.username + "avatar.jpg"
                                , sourceFile)
//                        }
                        uploadFile.saveInBackground(object: SaveCallback(){
                            override fun done(p0: AVException?) {
                                val mapObject = AVObject("ComfyUserInfoMap")
                                mapObject.put("user", userObject)
                                mapObject.put("avatar", uploadFile)
                                mapObject.saveInBackground(object :SaveCallback(){
                                    override fun done(p0: AVException?) {
                                        view.uploadAvatarFinish(uploadFile.url)
                                        try {
                                            view.avatarDao.deleteAll()
                                            val historyAvatar = GDAvatar(User.username, uploadFile.url)
                                            view.avatarDao.insert(historyAvatar)
                                        }
                                        catch (e: Exception){
                                            e.printStackTrace()
                                        }
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