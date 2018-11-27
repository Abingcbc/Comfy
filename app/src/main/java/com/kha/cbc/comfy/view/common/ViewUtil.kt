package com.kha.cbc.comfy.view.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.toast(text: String, length: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, text, length).show()
}

fun View.yum(text: String, length: Int = Snackbar.LENGTH_SHORT) : Snackbar{
    return Snackbar.make(this, text, length)
}

object ActivityManager{
    private val allActivities: MutableList<Activity> = mutableListOf()

    operator fun plusAssign(activity: Activity){
        allActivities += activity
    }

    operator fun minusAssign(activity: Activity){
        if(activity.isFinishing){
            allActivities -= activity
        }
        else{
            activity.finish()
            allActivities -= activity
        }
    }

    fun finishAll(){
        for(activity in allActivities){
            if(!activity.isFinishing){
                activity.finish()
            }
        }
        allActivities.clear()
    }
}