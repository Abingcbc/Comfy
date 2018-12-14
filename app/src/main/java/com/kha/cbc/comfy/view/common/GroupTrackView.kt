package com.kha.cbc.comfy.view.common

import android.view.View
import android.widget.ImageView
import com.amap.api.maps.AMap
import com.amap.api.track.AMapTrackClient
import com.amap.api.track.query.entity.Point

interface GroupTrackView{
    var yumLayout: View
    var amapTrackClient: AMapTrackClient
    var map: AMap
    var trackList: MutableList<String>
    fun onServiceStarted()
    fun onResultRetrieved(pair: Pair<String, Point>)
    fun onServiceBroken()
    fun onAvatarDownloadComplete(pairList: MutableList<Pair<String, String>>)
}