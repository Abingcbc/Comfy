package com.kha.cbc.comfy.view.efficient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.model.CustomUsageStats
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class UsageAdapter(val appUsageList: MutableList<CustomUsageStats>): RecyclerView.Adapter<UsageAdapter.ViewHolder>(){

    private val dateFormater = SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA)

//    private val totalTimeFormater = SimpleDateFormat("HH 小时, mm 分钟, ss 秒", Locale.CHINA)

    override fun getItemCount(): Int {
        return appUsageList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_usage_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = appUsageList[position]
        holder.appNameView.text = app.appName!!
        holder.timeView.text = String.format("Last Time Used: %s" , dateFormater.format(app.usageStats!!.lastTimeUsed))
        val totalTimeInForeground = app.usageStats!!.totalTimeInForeground
        holder.overallTimeView.text = String.format("Total Used: %s hours, %s minutes, %s seconds",
            (totalTimeInForeground/ (1000*60)) % 60,
            (totalTimeInForeground / 1000) % 60,
            (totalTimeInForeground / (1000*60*60)) % 24)
        Glide.with(holder.itemView).load(app.appIcon).into(holder.thumbnail)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var appNameView: TextView = itemView.findViewById(R.id.app_name)
        var timeView: TextView = itemView.findViewById(R.id.app_time)
        var thumbnail: CircleImageView = itemView.findViewById(R.id.app_thumbnail)
        var overallTimeView : TextView = itemView.findViewById(R.id.app_overall_time)
    }
}