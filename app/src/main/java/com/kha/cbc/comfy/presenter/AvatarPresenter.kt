package com.kha.cbc.comfy.presenter

import android.net.Uri
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVFile
import com.avos.avoscloud.ProgressCallback
import com.avos.avoscloud.SaveCallback
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.view.common.AvatarView


class AvatarPresenter(override val view: AvatarView): LeanCloudPresenter(view){
    fun uploadAvatar(uri: Uri){
        view.setProgressBarVisible()

        


        val sourceFile = uri.path
        val uploadFile = AVFile.withAbsoluteLocalPath(User.username + "avatar.jpg", sourceFile)
        uploadFile.saveInBackground(object: SaveCallback(){
            override fun done(p0: AVException?) {
                view.uploadAvatarFinish(uploadFile.url)
            }
        },object: ProgressCallback(){
            override fun done(p0: Int?) {
                view.uploadProgressUpdate(p0)
            }
        })
    }
}