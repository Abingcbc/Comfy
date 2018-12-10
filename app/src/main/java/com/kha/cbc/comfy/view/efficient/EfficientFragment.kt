package com.kha.cbc.comfy.view.efficient

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.view.efficient.chart.EfficientChartActivity
import com.leon.lib.settingview.LSettingItem

class EfficientFragment : Fragment(){
    lateinit var fragmentView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.efficient_menu, container, false)
        initTouch()
        return fragmentView
    }

    private fun initTouch(){
        val appUsageItem = fragmentView.findViewById<LSettingItem>(R.id.recent_app)
        appUsageItem.setmOnLSettingItemClick {
            val intent = Intent(activity, UsageActivity::class.java)
            startActivity(intent)
        }
        val appUsageChartItem = fragmentView.findViewById<LSettingItem>(R.id.recent_graph)
        appUsageItem.setmOnLSettingItemClick {
            val intent = Intent(activity, EfficientChartActivity::class.java)
            startActivity(intent)
        }
    }
}