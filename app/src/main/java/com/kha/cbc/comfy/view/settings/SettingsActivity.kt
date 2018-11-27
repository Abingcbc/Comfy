package com.kha.cbc.comfy.view.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.view.common.ActivityManager
import com.kha.cbc.comfy.view.settings.user.UserSettingActivity
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initSettingButtons()
        ActivityManager += this

    }

    private fun initSettingButtons(){
        user_info_item.setmOnLSettingItemClick{
            val intent = Intent(this, UserSettingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        ActivityManager -= this
        super.onDestroy()
    }
}
