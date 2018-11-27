package com.kha.cbc.comfy.view.common

import android.content.SharedPreferences


interface AvatarView: BaseView{

    fun uploadAvatarFinish(url: String)

    fun uploadProgressUpdate(progress: Int?)

    fun setProgressBarVisible()

    fun downloadAvatarFinish(url: String)
}