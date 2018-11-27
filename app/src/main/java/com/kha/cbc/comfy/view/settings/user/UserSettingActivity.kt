package com.kha.cbc.comfy.view.settings.user

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kha.cbc.comfy.ComfyApp
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.presenter.AvatarPresenter
import com.kha.cbc.comfy.view.common.ActivityManager
import com.kha.cbc.comfy.view.common.AvatarView
import com.kha.cbc.comfy.view.common.BaseActivityWithPresenter
import com.kha.cbc.comfy.view.common.yum
import com.kha.cbc.comfy.view.main.MainActivity
import com.leon.lib.settingview.LSettingItem
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_user_setting.*
import java.io.File

class UserSettingActivity : BaseActivityWithPresenter() , AvatarView{

    override fun downloadAvatarFinish(url: String) {
        Glide.with(this).load(url).into(avatar)
    }

    override val presenter = AvatarPresenter(this)

    override fun setProgressBarVisible(){
        upload_avatar_progress.visibility = View.VISIBLE
    }

    override fun uploadAvatarFinish(url: String) {
        upload_avatar_progress.visibility = View.GONE
        Log.d(this.toString(),  url)
    }

    override fun uploadProgressUpdate(progress: Int?) {
        if(progress != null){
            upload_avatar_progress.progress = progress
        }
    }

    private val CHOOSE_PHOTO = 1

    //TODO:avatar presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)
        initUserSettingView()
        presenter.loadAvatar()
        ActivityManager += this
    }

    private fun initUserSettingView(){
        copy_sessionToken.setRightText(User.sessionToken)
        copy_sessionToken.setLeftText("SessionToken:")
        copy_sessionToken.setmOnLSettingItemClick {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.primaryClip = ClipData.newPlainText("sessionToken", User.sessionToken)
            user_setting_layout.yum("SessionToken Copied to Clipboard").show()
        }
        username.setLeftText("Username:")
        username.setRightText(User.username)
        username.setmOnLSettingItemClick {
            val fragmentTransac = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("set_username")
            if(prev != null){
                fragmentTransac.remove(prev)
            }
            fragmentTransac.addToBackStack(null)

            val newDialog = UsernameSetDialog()
            newDialog.show(fragmentTransac,"set_username")
        }

        log_out.setOnClickListener {
            val userDao = (application as ComfyApp).daoSession.gdUserDao
            userDao.deleteAll()
            User.username = null
            User.sessionToken = null
            ActivityManager.finishAll()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        avatar.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    ,1)
            }
            else {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                startActivityForResult(intent, CHOOSE_PHOTO)
            }
        }


    }

    class UsernameSetDialog: DialogFragment(){
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            val inflater = activity!!.layoutInflater
            val view = inflater.inflate(R.layout.set_username_dialog, null)
            builder.setView(view)
                .setPositiveButton("change"){_,_ ->
                    val usernameEdit: EditText = view.findViewById(R.id.dialog_username)
                    User.username = usernameEdit.text.toString()
                    val userSetting: LSettingItem = (activity)!!.findViewById(R.id.username)
                    userSetting.setRightText(usernameEdit.text.toString())
                }
                .setNegativeButton("cancel"){_,_ -> this@UsernameSetDialog.dialog.cancel()}
                .setTitle("Change Username")
            return builder.create()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            CHOOSE_PHOTO -> {
                if(resultCode == Activity.RESULT_OK){

                    val options = UCrop.Options()
                    options.setAllowedGestures(UCropActivity.ALL,UCropActivity.ALL,UCropActivity.NONE)
                    options.setCircleDimmedLayer(true)
                    options.setShowCropFrame(false)
                    options.setShowCropGrid(false)


                    val sourceUri = data!!.data!!
                    val uCropTemp = File(this.externalCacheDir, "uCrop.jpg")
                    if(uCropTemp.exists()){
                        uCropTemp.delete()
                    }
                    val destinationUri = Uri.fromFile(File(this.externalCacheDir, "uCrop.jpg"))
                    UCrop.of(sourceUri, destinationUri)
                        .withOptions(options)
                        .withAspectRatio(1.toFloat(), 1.toFloat())
                        .start(this)
                }
            }
            UCrop.REQUEST_CROP -> {
                if(resultCode==RESULT_OK){
                    val resultUri= Uri.fromFile(File(this.externalCacheDir, "uCrop.jpg"))
                    val circleImageView = this.findViewById<CircleImageView>(R.id.avatar)
                    val options = RequestOptions().skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                    Glide.with(this).load(resultUri).apply(options).into(circleImageView)
                    presenter.uploadAvatar(resultUri)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager -= this
    }
}


