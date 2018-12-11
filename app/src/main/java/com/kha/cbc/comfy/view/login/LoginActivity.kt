package com.kha.cbc.comfy.view.login

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.kha.cbc.comfy.ComfyApp
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.entity.GDUser
import com.kha.cbc.comfy.greendao.gen.GDAvatarDao
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.presenter.AvatarPresenter
import com.kha.cbc.comfy.presenter.LoginPresenter
import com.kha.cbc.comfy.view.common.ActivityManager
import com.kha.cbc.comfy.view.common.AvatarView
import com.kha.cbc.comfy.view.common.BaseActivityWithPresenter
import com.kha.cbc.comfy.view.common.yum
import com.kha.cbc.comfy.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import shem.com.materiallogin.DefaultLoginView
import shem.com.materiallogin.DefaultRegisterView
import shem.com.materiallogin.MaterialLoginView
import java.io.File
import java.io.FileOutputStream


class LoginActivity : BaseActivityWithPresenter(), LoginView , AvatarView{

    override lateinit var avatarDao: GDAvatarDao

    override fun uploadAvatarFinish(url: String) {}

    override fun downloadAvatarFinish(urlPairs: MutableList<Pair<String, String>>) {}

    override fun uploadProgressUpdate(progress: Int?) {}

    override fun setProgressBarVisible() {}

    override fun downloadAvatarFinish(url: String) {}

    override val presenter = LoginPresenter(this)

    val avatarPresenter = AvatarPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ((login as MaterialLoginView).registerView as DefaultRegisterView).setListener { registerUser, registerPass,
                                                                                         registerPassRep ->
            val passRep = registerPassRep.editText!!.text.toString()
            val pass = registerPass.editText!!.text.toString()
            val user = registerUser.editText!!.text.toString()
            if (passRep != pass) {
                login.yum("Two passwords didn't match").show()
            } else if (pass == "" || user == "") {
                login.yum("Invalid Username or Password").show()
            } else {
                presenter.onRegister(user, pass)
            }
        }
        ((login as MaterialLoginView).loginView as DefaultLoginView).setListener { loginUser, loginPass ->
            val pass = loginPass.editText!!.text.toString()
            val user = loginUser.editText!!.text.toString()
            if (user == "" || pass == "") {
                login.yum("Invalid Username or Password").show()
            } else {
                presenter.onLogin(user, pass)
            }
        }

        ActivityManager += this
    }

    override fun onRegisterComplete(user: User) {
        login.yum("Successfully registered, auto login").show()
        User.username = user.username
        User.sessionToken = user.sessionToken
        User.comfyUserObjectId = user.comfyUserObjectId
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", user.username)
        intent.putExtra("sessionToken", user.sessionToken)
        val userDao = (application as ComfyApp).daoSession.gdUserDao
        userDao.insert(GDUser(user))
        val st = user.comfyUserObjectId
        startActivity(intent)
        val defaulticBitmap = BitmapFactory.decodeResource(resources, R.drawable.default_avatar)
        val dir = File(cacheDir, "temp_avatar")
        if(!dir.exists()){
            dir.mkdir()
        }
        val file = File(dir, "temp_avatar.jpg")
        try {
            val fos = FileOutputStream(file)
            defaulticBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        avatarPresenter.initiateAvatar(file)
        this.finish()
    }

    override fun onRegisterError(error: Throwable) {
        login.yum("Failed to create an account for you, please try again").show()
    }

    override fun onLoginComplete(user: User) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", user.username)
        intent.putExtra("sessionToken", user.sessionToken)
        val userDao = (application as ComfyApp).daoSession.gdUserDao
        userDao.insert(GDUser(user))
        startActivity(intent)
        this.finish()
    }

    override fun onLoginError(error: Throwable) {
        login.yum("Cannot login, please check your password-username pairs").show()
    }

    override fun onDestroy() {
        ActivityManager -= this
        super.onDestroy()
    }
}
