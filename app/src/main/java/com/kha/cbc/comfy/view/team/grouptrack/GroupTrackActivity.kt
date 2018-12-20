package com.kha.cbc.comfy.view.team.grouptrack

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amap.api.maps.AMap
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.track.AMapTrackClient
import com.amap.api.track.query.entity.Point
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import com.avos.avoscloud.FindCallback
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.kha.cbc.comfy.ComfyApp
import com.kha.cbc.comfy.GlideApp
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.greendao.gen.GDAvatarDao
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.presenter.AvatarPresenter
import com.kha.cbc.comfy.presenter.GroupTrackPresenter
import com.kha.cbc.comfy.view.common.AvatarView
import com.kha.cbc.comfy.view.common.GroupTrackView
import com.kha.cbc.comfy.view.common.yum
import kotlinx.android.synthetic.main.activity_group_track.*


class GroupTrackActivity : AppCompatActivity() , GroupTrackView, AvatarView{

    lateinit var avatarPresenter: AvatarPresenter

    val historyMarkerListPair = mutableListOf<Pair<String, Marker>>()


    override lateinit var yumLayout: View

    override lateinit var taskObjectId: String

    //Pair<userId, avatarurl>
    lateinit var userIdAvatarPairList: MutableList<Pair<String, String>>

    override fun onServiceStarted() {
        yumLayout.yum("Self-Track Opened")
    }

    override fun onResultRetrieved(pair: Pair<String, Point>) {
        val userId = pair.first
        val point = pair.second
        val query = AVQuery<AVObject>("ComfyUser")
        query.whereEqualTo("objectId", userId)
        query.findInBackground(object: FindCallback<AVObject>(){
            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                if(p0 != null && p0.size > 0){
                    val username = p0[0].getString("username")
                    val markerOptions = MarkerOptions()
                    val avatarUrlPair = userIdAvatarPairList.find {
                        it.first == userId
                    }
                    if(avatarUrlPair != null){
                        val avatarUrl = avatarUrlPair.second
                        GlideApp.with(this@GroupTrackActivity).asBitmap().load(avatarUrl).into(
                            object: SimpleTarget<Bitmap>(){
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    val matrix = Matrix()
                                    matrix.setScale(0.08f, 0.08f)
                                    val bitmap = Bitmap.createBitmap(
                                        resource, 0, 0, resource.width, resource.height, matrix, true
                                    )
                                    val historyMarker = historyMarkerListPair.find {
                                        it.first == userId
                                    }
                                    historyMarker?.second?.destroy()
                                    if(historyMarker != null){
                                        historyMarkerListPair.remove(historyMarker)
                                    }
                                    markerOptions.position(LatLng(point.lat, point.lng))
                                        .title(username)
                                        .snippet(username + "(" + point.lat.toString() + " " + point.lng.toString() + ")")
                                        .draggable(false)
                                        .visible(true)
                                        .setFlat(true)
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                        .infoWindowEnable(true)
                                    historyMarkerListPair.add(Pair(userId, map.addMarker(markerOptions)))
                                }
                            }
                        )
                    }

                }
            }
        })

    }

    override fun onServiceBroken() {

    }

    override fun onAvatarDownloadComplete(pairList: MutableList<Pair<String, String>>) {
        userIdAvatarPairList = pairList
        groupTrackPresenter.startService()
    }

    override lateinit var avatarDao: GDAvatarDao

    override fun uploadAvatarFinish(url: String) {}
    override fun downloadAvatarFinish(urlPairs: MutableList<Pair<String, String>>) {}
    override fun uploadProgressUpdate(progress: Int?) {}
    override fun setProgressBarVisible() {}
    override fun downloadAvatarFinish(url: String) {}

    //TODO: 开启定位/蓝牙？

    val RC_PERMISSIONS = 1

    override lateinit var map: AMap

    override lateinit var trackList: MutableList<String>

    override lateinit var amapTrackClient: AMapTrackClient

    lateinit var groupTrackPresenter: GroupTrackPresenter


//    val onTrackLifecycleListener = object: OnTrackLifecycleListener{
//        override fun onStartGatherCallback(p0: Int, p1: String?) {
//            if (p0 == ErrorCode.TrackListen.START_GATHER_SUCEE ||
//                p0 == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
//                group_track_layout.yum("定位采集开启成功！").show()
//            } else {
//                group_track_layout.yum("定位采集启动异常，").show()
//            }
//        }
//
//        override fun onStopTrackCallback(p0: Int, p1: String?) {
//
//        }
//
//        override fun onBindServiceCallback(p0: Int, p1: String?) {
//
//        }
//
//        override fun onStopGatherCallback(p0: Int, p1: String?) {
//
//        }
//
//        override fun onStartTrackCallback(p0: Int, p1: String?) {
//            if (p0 == ErrorCode.TrackListen.START_TRACK_SUCEE ||
//                p0 == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
//                p0 == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
//                // 服务启动成功，继续开启收集上报
//                aMapTrackClient.startGather(this)
//            } else {
//                group_track_layout.yum("轨迹上报服务服务启动异常，").show()
//            }
//        }
//    }

    private fun initService(){
        map = group_track_amap.map
        amapTrackClient = AMapTrackClient(applicationContext)
        groupTrackPresenter.fetchMembers()
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        group_track_amap.onDestroy()
        groupTrackPresenter.onViewDestroyed()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        group_track_amap.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        group_track_amap.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        group_track_amap.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_track)
        group_track_amap.onCreate(savedInstanceState)
        taskObjectId = intent.getStringExtra("taskObjectId")

        if (
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            )
                ,RC_PERMISSIONS)
        }
        else{
            groupTrackPresenter = GroupTrackPresenter(this)
            avatarPresenter = AvatarPresenter(this)
            avatarDao = (application as ComfyApp).daoSession.gdAvatarDao
            yumLayout = group_track_layout
            //
            initService()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            RC_PERMISSIONS -> {
                var isPermitted = true
                for(permission in permissions){
                    if(grantResults[permissions.indexOf(permission)] != PackageManager.PERMISSION_GRANTED){
                        isPermitted = false
                        group_track_layout.yum("You Denied the permission").show()
                        break
                    }
                }
                if(isPermitted){
                    groupTrackPresenter = GroupTrackPresenter(this)
                    avatarPresenter = AvatarPresenter(this)
                    avatarDao = (application as ComfyApp).daoSession.gdAvatarDao
                    yumLayout = group_track_layout
                    //
                    initService()
                }
            }
        }
    }
}
