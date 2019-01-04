package com.kha.cbc.comfy.data.network

import com.kha.cbc.comfy.data.network.dto.TeamTaskDto
import com.kha.cbc.comfy.data.network.dto.UserInfoDto
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface LeanCloudApi {

// Example:
// @GET("characters")
//    fun getCharacters(
//        @Query("offset") offset: Int?,
//        @Query("nameStartsWith") searchQuery: String?,
//        @Query("limit") limit: Int?
//    ): Single<DataWrapper<List<CharacterMarvelDto>>>

    //register

    @POST("users")
    fun postNewregister(
        @Body requestBody: RequestBody
    ): Single<UserInfoDto>

    //login
    @POST("login")
    fun getAccount(
        @Body requestBody: RequestBody
    ): Single<UserInfoDto>

    @POST("classes")
    fun postTeamTask(
        @Body requestBody: RequestBody
    ): Single<TeamTaskDto>

//    @GET("classes")
//        fun loadAllCreateTask(
//
//    )

}