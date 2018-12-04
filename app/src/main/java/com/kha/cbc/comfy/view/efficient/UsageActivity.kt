package com.kha.cbc.comfy.view.efficient

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.CustomUsageStats
import com.kha.cbc.comfy.view.common.yum
import kotlinx.android.synthetic.main.activity_usage.*


class UsageActivity : AppCompatActivity() {

    private lateinit var usageStatsManager: UsageStatsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usage)
        usageStatsManager = this.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        app_usage_recycler.layoutManager = LinearLayoutManager(this)
        app_usage_recycler.adapter = UsageAdapter(getUsageList())
    }

    private fun getUsageList(): MutableList<CustomUsageStats>{
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)

        //TODO: Add Interval Choosing
        val queryUsageStats = usageStatsManager
            .queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis,
                System.currentTimeMillis()
            )

        if (queryUsageStats.size == 0) {
            app_usage_recycler.yum("App usage access not allowed").setAction("Go and Set") {
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }.show()
        }
        val resultList: MutableList<CustomUsageStats> = mutableListOf()
        for(appStats in queryUsageStats){
            val customUsage = CustomUsageStats()
            customUsage.usageStats = appStats
            try {
                val appIcon = this.packageManager
                    .getApplicationIcon(appStats.packageName)
                customUsage.appIcon = appIcon
            } catch (e: PackageManager.NameNotFoundException) {
                Log.w(
                    "Usage Activity", String.format(
                        "App Icon is not found for %s",
                        appStats.packageName
                    )
                )
                customUsage.appIcon = this.getDrawable(R.mipmap.ic_launcher_round)
            }
            resultList.add(customUsage)
        }


        resultList.sortByDescending {
            it.usageStats!!.lastTimeUsed
        }
        val finalResult: MutableList<CustomUsageStats> = mutableListOf()
        resultList.filter {
            val stringList = it.usageStats!!.packageName.split(".")
            if(stringList[0] == "com" || stringList[0] ==  "jp" || stringList[0] == "gg"){
                when(stringList[1]){
                    "huawei" -> {
                        false
                    }
                    "android" -> {
                        false
                    }
                    else -> {
                        true
                    }
                }
            }
            else{
                false
            }
        }.forEach {
            val stringList = it.usageStats!!.packageName.split(".")
            val appName = stringList.last()
            val finalUsageStats = CustomUsageStats()
            finalUsageStats.usageStats = it.usageStats
            finalUsageStats.appIcon = it.appIcon
            finalUsageStats.appName = appName
            finalResult.add(finalUsageStats)
        }

        return finalResult
    }
}
