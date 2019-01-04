package com.kha.cbc.comfy.data

import com.kha.cbc.comfy.data.network.LeanCloudApi
import com.kha.cbc.comfy.data.network.providers.retrofit
import com.kha.cbc.comfy.model.TeamTask
import com.kha.cbc.comfy.model.User
import io.reactivex.Single
import okhttp3.RequestBody
import org.json.JSONObject

class LeanCloudRepositoryImpl : LeanCloudRepository {

    val api = retrofit.create(LeanCloudApi::class.java)
    override fun registerNewUser(username: String, password: String): Single<User> {
        //TODO:Json数据提交
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        val requestBody = RequestBody.create(JSONMediaType, json.toString())
        //TODO:尝试直接抛入JSONObject?
        return api.postNewregister(requestBody).map(User::fromUserInfoDto)
    }

    override fun login(username: String, password: String): Single<User> {
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        val requestBody = RequestBody.create(JSONMediaType, json.toString())
        return api.getAccount(requestBody).map(User::fromUserInfoDto)
    }


    override fun uploadTeamTask(taskTitle: String, createUserName: String): Single<TeamTask> {
        val json = JSONObject()
        json.put("TaskTitle", taskTitle)
        json.put("CreateUserName", createUserName)
        val requestBody = RequestBody.create(JSONMediaType, json.toString())
        //TODO:传入图片
        return api.postTeamTask(requestBody).map(::TeamTask)
    }
}