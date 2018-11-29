package com.kha.cbc.comfy.view.common

import android.content.SharedPreferences
import com.kha.cbc.comfy.greendao.gen.GDAvatarDao


interface AvatarView: BaseView{


    var avatarDao: GDAvatarDao
    fun uploadAvatarFinish(url: String)

    fun uploadProgressUpdate(progress: Int?)

    fun setProgressBarVisible()

    fun downloadAvatarFinish(url: String)
}