package com.kha.cbc.comfy.model

import com.kha.cbc.comfy.data.network.dto.UserInfoDto
import com.kha.cbc.comfy.entity.GDUser

//TODO: encryption

object User {
    var username: String? = null
    var sessionToken: String? = null
    var comfyUserObjectId: String? = null

    fun fromUserInfoDto(dto: UserInfoDto): User {
        username = dto.username
        sessionToken = dto.sessionToken
        return User
    }

    fun fromGDUser(gdUser: GDUser): User {
        username = gdUser.username
        sessionToken = gdUser.sessionToken
        comfyUserObjectId = gdUser.objectId
        return User
    }
}

//data class User(
//    val username: String?,
//    val sessionToken: String?
//    //有默认set与get吗？
//){
//    //TODO: DTO Connection
//    constructor(dto: UserInfoDto): this(
//        username = dto.username,
//        sessionToken = dto.sessionToken
//    )
//}