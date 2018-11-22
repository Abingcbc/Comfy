package com.kha.cbc.comfy.model

import com.kha.cbc.comfy.data.network.dto.UserInfoDto
import com.kha.cbc.comfy.entity.GDUser

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
}