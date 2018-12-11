package com.kha.cbc.comfy.view.efficient.chart

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.ClickTimes
import com.kha.cbc.comfy.model.CustomUsageStats
import com.kha.cbc.comfy.view.common.yum
import com.kha.cbc.comfy.view.efficient.chart.fragments.LaunchTimesFragment
import kotlinx.android.synthetic.main.activity_efficient_chart.*
import kotlinx.android.synthetic.main.activity_usage.*
import java.text.SimpleDateFormat
import java.util.*

class EfficientChartActivity : AppCompatActivity() {

    private val dateFormater = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)

    private lateinit var usageStatsManager: UsageStatsManager

    private lateinit var usageListDaily: MutableList<CustomUsageStats>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_efficient_chart)
        usageStatsManager = this.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        usageListDaily = getUsageList(UsageStatsManager.INTERVAL_DAILY)
        val fragmentList : MutableList<Fragment> = mutableListOf()
        val launchTimesFragment = LaunchTimesFragment()
        val launchTimesBundle = Bundle()
        launchTimesBundle.putParcelableArrayList("clicks", getLaunchTimesList() as ArrayList<out Parcelable>)
        launchTimesFragment.arguments = launchTimesBundle
        fragmentList.add(launchTimesFragment)
        val pagerAdapter = EfficientChartPagerAdapter(fragmentList, supportFragmentManager)
        efficient_chart_viewpager.adapter = pagerAdapter
    }

    private fun getLaunchTimesList(): ArrayList<ClickTimes>{
        var result = mutableListOf<ClickTimes>()
        var temp = usageListDaily.sortedByDescending {
            it.usageStats!!.lastTimeUsed
        }
        val latestTime = temp.first().usageStats!!.lastTimeUsed
        temp = temp.filter {
            dateFormater.format(it.usageStats!!.lastTimeUsed) == dateFormater.format(latestTime)
        }
        temp.forEach {
            val field = UsageStats::class.java.getDeclaredField("mLaunchCount")
            val newClickTimes = ClickTimes(it.appName, field.getInt(it.usageStats))
            result.add(newClickTimes)
        }
        result.sortByDescending {
            it.launchTimes
        }
        result = ArrayList(result.subList(0, 5))
        return result
    }

    //UsageStatsManager.INTERVAL_DAILY
    private fun getUsageList(interval: Int): MutableList<CustomUsageStats>{
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)

        //TODO: Add Interval Choosing
        val queryUsageStats = usageStatsManager
            .queryUsageStats(
                interval, cal.timeInMillis,
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
