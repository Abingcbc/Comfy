package com.kha.cbc.comfy.view.common

interface UserServiceView :BaseView{
    fun usernameChangeFinished()
    fun usernameChangeFailed()
    fun passwordChangeFinished()
    fun passwordChangeFailed()
}