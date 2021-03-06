package com.kha.cbc.comfy.view.common

import com.kha.cbc.comfy.greendao.gen.GDAvatarDao


interface AvatarView: BaseView{
    var avatarDao: GDAvatarDao
    fun uploadAvatarFinish(url: String)
    fun downloadAvatarFinish(urlPairs: MutableList<Pair<String, String>>)

    fun uploadProgressUpdate(progress: Int?)

    fun setProgressBarVisible()

    fun downloadAvatarFinish(url: String)
}