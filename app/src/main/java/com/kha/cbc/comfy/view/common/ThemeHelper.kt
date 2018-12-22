package com.kha.cbc.comfy.view.common

import android.R.id.edit
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.bilibili.magicasakura.utils.ThemeUtils
import com.google.android.material.navigation.NavigationView
import com.kha.cbc.comfy.R


object ThemeHelper {
    private val CURRENT_THEME = "theme_current"

    val CARD_SAKURA = 0x1
    val CARD_HOPE = 0x2
    val CARD_STORM = 0x3
    val CARD_WOOD = 0x4
    val CARD_LIGHT = 0x5
    val CARD_THUNDER = 0x6
    val CARD_SAND = 0x7
    val CARD_FIREY = 0x8
    val CARD_DARK = 0x9

    fun getSharePreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("multiple_theme", Context.MODE_PRIVATE)
    }

    fun setTheme(context: Context, themeId: Int) {
        getSharePreference(context).edit()
            .putInt(CURRENT_THEME, themeId)
            .apply()
    }

    fun getTheme(context: Context): Int {
        return getSharePreference(context).getInt(CURRENT_THEME, CARD_SAKURA)
    }

    fun isDefaultTheme(context: Context): Boolean {
        return getTheme(context) == CARD_SAKURA
    }

    fun commonRefresh(activity: Activity){
        ThemeUtils.refreshUI(activity, object : ThemeUtils.ExtraRefreshable {
            override fun refreshGlobal(activity: Activity) {
                //for global setting, just do once

                //dark Mode
//                if(ThemeHelper.isDarkThemeById(getTheme(activity))) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                }
//                else{
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                }
                activity.window.statusBarColor = ThemeUtils.getColorById(activity, R.color.theme_color_primary_dark)
            }

            override fun refreshSpecificView(view: View) {
                //TODO: will do this for each traversal
            }
        })
    }

    fun isDarkThemeById(id: Int): Boolean{
        return id == CARD_DARK
    }

    fun getName(currentTheme: Int): String {
        when (currentTheme) {
            CARD_SAKURA -> return "THE SAKURA"
            CARD_STORM -> return "THE STORM"
            CARD_WOOD -> return "THE WOOD"
            CARD_LIGHT -> return "THE LIGHT"
            CARD_HOPE -> return "THE HOPE"
            CARD_THUNDER -> return "THE THUNDER"
            CARD_SAND -> return "THE SAND"
            CARD_FIREY -> return "THE FIREY"
            CARD_DARK -> return "THE DARK"
        }
        return "THE RETURN"
    }
}