package com.kha.cbc.comfy.view.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.view.common.ActivityManager
import com.kha.cbc.comfy.view.common.ThemeHelper
import com.kha.cbc.comfy.view.guide.GuideActivity
import com.kha.cbc.comfy.view.settings.user.UserSettingActivity
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton
import com.nightonke.boommenu.BoomMenuButton
import com.nightonke.boommenu.Piece.PiecePlaceEnum
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    var currentTheme = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initSettingButtons()
        currentTheme = ThemeHelper.getTheme(this)
        ThemeHelper.commonRefresh(this)
        ActivityManager += this
    }

    private fun initSettingButtons(){
        user_info_item.setmOnLSettingItemClick{
            val intent = Intent(this, UserSettingActivity::class.java)
            startActivity(intent)
        }
        help_item.setmOnLSettingItemClick {
            val intentGuide = Intent(this, GuideActivity::class.java)
            intentGuide.putExtra("return", "com.kha.cbc.comfy.view.settings.SettingsActivity")
            startActivity(intentGuide)
            finish()
        }
    }

    private fun changeTheme(){
        if (ThemeHelper.getTheme(this) != currentTheme) {
//            if(ThemeHelper.isDarkThemeById(currentTheme)) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            }
//            else{
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
            ThemeHelper.setTheme(this, currentTheme)
            ThemeHelper.commonRefresh(this)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val changeThemeButton = findViewById<BoomMenuButton>(R.id.change_theme_button)
        currentTheme = ThemeHelper.getTheme(this)
//        if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_STORM) {
//            return "blue"
//        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_HOPE) {
//            return "purple"
//        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_WOOD) {
//            return "green"
//        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_LIGHT) {
//            return "green_light"
//        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_THUNDER) {
//            return "yellow"
//        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAND) {
//            return "orange"
//        } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_FIREY) {
//            return "red"
//        }
        changeThemeButton.piecePlaceEnum = PiecePlaceEnum.DOT_8_1
        changeThemeButton.buttonPlaceEnum = ButtonPlaceEnum.SC_8_1
        changeThemeButton.clearBuilders()
        val blueBuilder = TextInsideCircleButton.Builder()
            .normalText("STORM")
            .rippleEffect(true)
            .normalColorRes(R.color.blue_dark)
            .listener{
                currentTheme = ThemeHelper.CARD_STORM
                changeTheme()
            }
        changeThemeButton.addBuilder(blueBuilder)
        val purpleBuilder = TextInsideCircleButton.Builder()
            .normalText("HOPE")
            .rippleEffect(true)
            .normalColorRes(R.color.purple_dark)
            .listener{
                currentTheme = ThemeHelper.CARD_HOPE
                changeTheme()
            }
        changeThemeButton.addBuilder(purpleBuilder)
        val greenBuilder = TextInsideCircleButton.Builder()
            .normalText("WOOD")
            .rippleEffect(true)
            .normalColorRes(R.color.green_dark)
            .listener{
                currentTheme = ThemeHelper.CARD_WOOD
                changeTheme()
            }
        changeThemeButton.addBuilder(greenBuilder)
        val greenLightBuilder = TextInsideCircleButton.Builder()
            .normalText("LIGHT")
            .rippleEffect(true)
            .normalColorRes(R.color.green_light_dark)
            .listener{
                currentTheme = ThemeHelper.CARD_LIGHT
                changeTheme()
            }
        changeThemeButton.addBuilder(greenLightBuilder)
        val yellowBuilder = TextInsideCircleButton.Builder()
            .normalText("THUNDER")
            .rippleEffect(true)
            .normalColorRes(R.color.yellow_dark)
            .listener{
                currentTheme = ThemeHelper.CARD_THUNDER
                changeTheme()
            }
        changeThemeButton.addBuilder(yellowBuilder)
        val orangeBuilder = TextInsideCircleButton.Builder()
            .normalText("SAND")
            .rippleEffect(true)
            .normalColorRes(R.color.orange_dark)
            .listener{
                currentTheme = ThemeHelper.CARD_SAND
                changeTheme()
            }
        changeThemeButton.addBuilder(orangeBuilder)
        val redBuilder = TextInsideCircleButton.Builder()
            .normalText("FIREY")
            .rippleEffect(true)
            .normalColorRes(R.color.red_dark)
            .listener{
                currentTheme = ThemeHelper.CARD_FIREY
                changeTheme()
            }
        changeThemeButton.addBuilder(redBuilder)
        val darkBuilder = TextInsideCircleButton.Builder()
            .normalText("DARK")
            .rippleEffect(true)
            .normalColorRes(R.color.dark)
            .listener{
                currentTheme = ThemeHelper.CARD_DARK
                changeTheme()
            }
        changeThemeButton.addBuilder(darkBuilder)
    }

    override fun onDestroy() {
        ActivityManager -= this
        super.onDestroy()
    }
}
