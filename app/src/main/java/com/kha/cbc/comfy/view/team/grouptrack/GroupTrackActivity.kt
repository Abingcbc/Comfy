package com.kha.cbc.comfy.view.team.grouptrack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amap.api.maps.AMap
import com.kha.cbc.comfy.R
import com.kha.cbc.comfy.view.common.yum
import kotlinx.android.synthetic.main.activity_group_track.*

class GroupTrackActivity : AppCompatActivity() {


    //TODO: 开启定位/蓝牙？

    val RC_PERMISSIONS = 1

    lateinit var map: AMap

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        group_track_amap.onDestroy()
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
                    initService()
                }
            }
        }
    }

    private fun initService(){
        map = group_track_amap.map
    }
}
