package com.kha.cbc.comfy.view.efficient.chart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.ClickTimes

class LaunchTimesFragment : Fragment(){

    lateinit var launchTimesView: View
    lateinit var chart: HorizontalBarChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        launchTimesView = inflater.inflate(R.layout.launch_times_fragment, container, false)
        chart = launchTimesView.findViewById(R.id.click_times_barchart)
        prepareData()
        return launchTimesView
    }

    private fun prepareData(){
        val launchTimesList = arguments!!.getParcelableArrayList<ClickTimes>("clicks")
                as MutableList<ClickTimes>
        launchTimesList.sortBy {
            it.appName
        }
        val names = mutableListOf<String>()
        launchTimesList.forEach {
            names.add(it.appName!!)
        }
        chart.description.isEnabled = false
        chart.setPinchZoom(true)
        val xAxis = chart.xAxis
//        xAxis.setCenterAxisLabels(true)
        xAxis.labelCount = launchTimesList.size
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.valueFormatter = IndexAxisValueFormatter(names)

        val entries = mutableListOf<BarEntry>()
        launchTimesList.forEach {
            entries.add(BarEntry(launchTimesList.indexOf(it).toFloat(), it.launchTimes!!.toFloat()))
        }

        val dataSet = BarDataSet(entries, "Click Times")

        val barData = BarData(dataSet)
        chart.data = barData
        chart.animateXY(1500, 3000)
    }
}