package com.kha.cbc.comfy.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.kha.cbc.comfy.data.network.LeanCloudApi
import com.kha.cbc.comfy.data.network.providers.retrofit
import com.kha.cbc.comfy.model.User
import io.reactivex.Single
import okhttp3.RequestBody
import org.json.JSONObject

class LeanCloudRepositoryImpl: LeanCloudRepository {
    val api = retrofit.create(LeanCloudApi::class.java)
    override fun registerNewUser(username: String, password: String): Single<User> {
        //TODO:Json数据提交
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        val requestBody = RequestBody.create(JSONMediaType, json.toString())
        //TODO:尝试直接抛入JSONObject?
        return api.postNewregister(requestBody).map (::User)
    }

    override fun login(username: String, password: String): Single<User>{
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)
        val requestBody = RequestBody.create(JSONMediaType, json.toString())
        return api.getAccount(requestBody).map (::User)
    }
}