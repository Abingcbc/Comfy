package com.kha.cbc.comfy.model

import android.app.usage.UsageStats
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable


class CustomUsageStats {
    var usageStats: UsageStats? = null
    var appIcon: Drawable? = null
    var appName: String? = null
}

class ClickTimes(var appName: String?, var launchTimes: Int?): Parcelable{

    val CREATOR: Parcelable.Creator<ClickTimes> = object : Parcelable.Creator<ClickTimes> {

        override fun createFromParcel(source: Parcel): ClickTimes {
            return ClickTimes(source.readString(), source.readInt())
        }

        override fun newArray(size: Int): Array<ClickTimes?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0!!.writeString(appName!!)
        p0.writeInt(launchTimes!!)
    }

    override fun describeContents(): Int {
        return 0
    }
}