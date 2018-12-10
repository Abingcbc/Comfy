package com.kha.cbc.comfy.view.efficient.chart

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class EfficientChartPagerAdapter(val fragmentList: MutableList<Fragment>, val fragmentManager: FragmentManager)
    : FragmentPagerAdapter(fragmentManager){

    //TODO:Top Five Used Apps + 启动次数
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int = fragmentList.size
}