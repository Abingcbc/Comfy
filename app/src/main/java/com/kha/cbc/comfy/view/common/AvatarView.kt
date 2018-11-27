package com.kha.cbc.comfy.view.common


interface AvatarView: BaseView{
    fun uploadAvatarFinish(url: String)

    fun uploadProgressUpdate(progress: Int?)

    fun setProgressBarVisible()

    fun downloadAvatarFinish()
}