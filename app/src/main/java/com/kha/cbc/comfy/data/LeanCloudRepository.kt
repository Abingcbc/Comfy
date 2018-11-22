package com.kha.cbc.comfy.data

import com.kha.cbc.comfy.model.User
import io.reactivex.Single

interface LeanCloudRepository {
    fun registerNewUser(username: String, password: String): Single<User>
    fun login(username: String, password:String): Single<User>
    //?
    companion object : Provider<LeanCloudRepository>(){
        override fun creator() = LeanCloudRepositoryImpl()
    }
}