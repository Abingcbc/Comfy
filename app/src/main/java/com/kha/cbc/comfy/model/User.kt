package com.kha.cbc.comfy.model

import com.kha.cbc.comfy.data.network.dto.UserInfoDto

//TODO: encryption
data class User(
    val username: String?,
    val sessionToken: String?
    //有默认set与get吗？
){
    //TODO: DTO Connection
    constructor(dto: UserInfoDto): this(
        username = dto.username,
        sessionToken = dto.sessionToken
    )
//    constructor(dto: UserDto): this(
//        name = dto.name,
//        imageUrl = dto.imageUrl
//    )
}