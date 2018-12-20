package com.kha.cbc.comfy.presenter

import android.os.AsyncTask
import com.amap.api.track.ErrorCode
import com.amap.api.track.OnTrackLifecycleListener
import com.amap.api.track.TrackParam
import com.amap.api.track.query.entity.DriveMode
import com.amap.api.track.query.entity.Point
import com.amap.api.track.query.model.*
import com.avos.avoscloud.*
import com.kha.cbc.comfy.BuildConfig
import com.kha.cbc.comfy.model.User
import com.kha.cbc.comfy.view.common.GroupTrackView
import com.kha.cbc.comfy.view.common.yum
import java.security.acl.Group
import java.util.*


class GroupTrackPresenter(val view: GroupTrackView){

    //STEPS: fetchAvatar -> startService -> startUploadTrack -> fetchDownloadPairs -> startDownloadAsyncTask

    class DownloadPointTimer(val downloadTargets: MutableList<Pair<Long, Long>>,
                             val view: GroupTrackView): TimerTask(){
        override fun run() {
            val targets = downloadTargets
            for(target in targets){
                val terminalId = target.first
                view.amapTrackClient.queryLatestPoint(LatestPointRequest(BuildConfig.COMFYGROUPTRACKSERVICEID.toLong(),
                    terminalId), object : OnTrackListener{
                    override fun onLatestPointCallback(p0: LatestPointResponse?) {
                        if (p0 != null && p0.isSuccess) {
                            val point = p0.latestPoint.point
                            val query = AVQuery<AVObject>("GroupTrack")
                            query.whereEqualTo("terminalId", terminalId)
                            query.findInBackground(object: FindCallback<AVObject>(){
                                override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                                    if(p0 != null && p0.size > 0){
                                        val userId = p0[0].getString("userId")
                                        val newPair = Pair<String, Point>(userId, point)
                                        view.onResultRetrieved(newPair)
                                    }
                                }
                            })
                            // 查询实时位置成功，point为实时位置信息
                        } else {
                            view.onServiceBroken()
                        }
                    }
                    override fun onCreateTerminalCallback(p0: AddTerminalResponse?) {}
                    override fun onQueryTrackCallback(p0: QueryTrackResponse?) {}
                    override fun onDistanceCallback(p0: DistanceResponse?) {}
                    override fun onQueryTerminalCallback(p0: QueryTerminalResponse?) {}
                    override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {}
                    override fun onParamErrorCallback(p0: ParamErrorResponse?) {}
                    override fun onAddTrackCallback(p0: AddTrackResponse?) {}
                })
            }
        }
    }

    //Pair: First:userId, Second:point
    class DownloadPointAsyncTask(val view: GroupTrackView) : AsyncTask<MutableList<Pair<Long, Long>>, Double, Unit>(){

        override fun doInBackground(vararg p0: MutableList<Pair<Long, Long>>?){
            val targets = p0[0]!!
            for(target in targets){
                val terminalId = target.first
                view.amapTrackClient.queryLatestPoint(LatestPointRequest(BuildConfig.COMFYGROUPTRACKSERVICEID.toLong(),
                    terminalId), object : OnTrackListener{
                    override fun onLatestPointCallback(p0: LatestPointResponse?) {
                        if (p0 != null && p0.isSuccess) {
                            val point = p0.latestPoint.point
                            val query = AVQuery<AVObject>("GroupTrack")
                            query.whereEqualTo("terminalId", terminalId)
                            query.findInBackground(object: FindCallback<AVObject>(){
                                override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                                    if(p0 != null && p0.size > 0){
                                        val userId = p0[0].getString("userId")
                                        val newPair = Pair<String, Point>(userId, point)
                                        view.onResultRetrieved(newPair)
                                    }
                                }
                            })
                            // 查询实时位置成功，point为实时位置信息
                        } else {
                            view.onServiceBroken()
                        }
                    }
                    override fun onCreateTerminalCallback(p0: AddTerminalResponse?) {}
                    override fun onQueryTrackCallback(p0: QueryTrackResponse?) {}
                    override fun onDistanceCallback(p0: DistanceResponse?) {}
                    override fun onQueryTerminalCallback(p0: QueryTerminalResponse?) {}
                    override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {}
                    override fun onParamErrorCallback(p0: ParamErrorResponse?) {}
                    override fun onAddTrackCallback(p0: AddTrackResponse?) {}
                })
            }
        }
    }

    class DownloadTrackAsyncTask(val view: GroupTrackView) : AsyncTask<MutableList<Pair<Long, Long>>, Double, Unit>(){

        override fun doInBackground(vararg p0: MutableList<Pair<Long, Long>>?){
            val targets = p0[0]!!
            for(target in targets){
                val terminalId = target.first
                val trackId = target.second
                val queryTrackRequest = QueryTrackRequest(
                    BuildConfig.COMFYGROUPTRACKSERVICEID.toLong(),
                    terminalId,
                    trackId, // 轨迹id，传-1表示查询所有轨迹
                    System.currentTimeMillis() - 12 * 60 * 60 * 1000,
                    System.currentTimeMillis(),
                    0, // 不启用去噪
                    1, // 绑路
                    0, // 不进行精度过滤
                    DriveMode.WALKING, // 当前仅支持驾车模式
                    1, // 距离补偿
                    5000, // 距离补偿，只有超过5km的点才启用距离补偿
                    1, // 结果应该包含轨迹点信息
                    1, // 返回第1页数据，由于未指定轨迹，分页将失效
                    100    // 一页不超过100条
                )
                view.amapTrackClient.queryTerminalTrack(queryTrackRequest, object: OnTrackListener{
                    override fun onLatestPointCallback(p0: LatestPointResponse?) {}
                    override fun onCreateTerminalCallback(p0: AddTerminalResponse?) {}
                    override fun onQueryTrackCallback(p0: QueryTrackResponse?) {
                        if (p0 != null && p0.isSuccess) {
                            val tracks = p0.tracks
                            val track = tracks[0]
                            val query = AVQuery<AVObject>("GroupTrack")
                            query.whereEqualTo("trackId", track.trid)
                            query.findInBackground(object: FindCallback<AVObject>(){
                                override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                                    if(p0 != null && p0.size > 0){
                                        val username = p0[0].getString("userId")
                                    }
                                }
                            })
                        } else {
                            view.onServiceBroken()

                        }
                    }
                    override fun onDistanceCallback(p0: DistanceResponse?) {}
                    override fun onQueryTerminalCallback(p0: QueryTerminalResponse?) {}
                    override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {}
                    override fun onParamErrorCallback(p0: ParamErrorResponse?) {}
                    override fun onAddTrackCallback(p0: AddTrackResponse?) {}
                })
            }
        }
    }

    lateinit var onTrackLifecycleListener: OnTrackLifecycleListener

    lateinit var terminalName : String

    val downloadPairs: MutableList<Pair<Long, Long>> = mutableListOf()

    var terminalId : Long = -1

    var trackId : Long = -1

    var serviceId : Long = BuildConfig.COMFYGROUPTRACKSERVICEID.toLong()

    lateinit var groupTrackAsyncTask: AsyncTask<MutableList<Pair<Long, Long>>, Double, Unit>

    lateinit var groupTrackTimerTask: TimerTask

    lateinit var timer: Timer

    fun fetchAvatar(){
        val pairList = mutableListOf<Pair<String, String>>()
        for(target in view.trackList){

            val userQuery = AVQuery<AVObject>("ComfyUser")
            userQuery.whereEqualTo("objectId", target)
            userQuery.findInBackground(object: FindCallback<AVObject>(){
                override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                    if(p0 != null && p0.size > 0){
                        val user = p0[0]
                        val query = AVQuery<AVObject>("ComfyUserInfoMap")
                        query.whereEqualTo("user", user)
                        query.include("avatar")
                        query.findInBackground(object: FindCallback<AVObject>(){
                            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                                if(p0 != null && p0.size > 0){
                                    val avatarFile = p0[0].getAVFile<AVFile>("avatar")
                                    pairList.add(Pair(target, avatarFile.url))
                                    if(pairList.size == view.trackList.size){
                                        view.onAvatarDownloadComplete(pairList)
                                    }
                                }
                            }
                        })
                    }
                }
            })
        }
    }

    fun startService(){

        terminalName = User.comfyUserObjectId!!

        //Start Track
        onTrackLifecycleListener = object: OnTrackLifecycleListener {
            override fun onStartGatherCallback(p0: Int, p1: String?) {
                if (p0 == ErrorCode.TrackListen.START_GATHER_SUCEE ||
                    p0 == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
                    view.yumLayout.yum("定位采集开启成功！").show()
                } else {
                    view.yumLayout.yum("定位采集启动异常，").show()
                }
            }
            override fun onStopTrackCallback(p0: Int, p1: String?) {}
            override fun onBindServiceCallback(p0: Int, p1: String?) {}
            override fun onStopGatherCallback(p0: Int, p1: String?) {}
            override fun onStartTrackCallback(p0: Int, p1: String?) {
                if (p0 == ErrorCode.TrackListen.START_TRACK_SUCEE ||
                    p0 == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
                    p0 == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                    // 服务启动成功，继续开启收集上报
                    view.amapTrackClient.startGather(this)
                } else {
                    view.yumLayout.yum("轨迹上报服务服务启动异常，").show()
                }
            }
        }

        //Create New TrackId
        view.amapTrackClient.queryTerminal(
            QueryTerminalRequest(
                BuildConfig.COMFYGROUPTRACKSERVICEID.toLong(),
                terminalName), object: OnTrackListener {

                override fun onLatestPointCallback(p0: LatestPointResponse?) {}
                override fun onCreateTerminalCallback(p0: AddTerminalResponse?) {}
                override fun onQueryTrackCallback(p0: QueryTrackResponse?) {}
                override fun onDistanceCallback(p0: DistanceResponse?) {}
                override fun onQueryTerminalCallback(p0: QueryTerminalResponse?) {
                    if(p0 != null && p0.isSuccess){
                        if(p0.tid <= 0){
                            //terminal 不存在，先创建
                            view.amapTrackClient.addTerminal(
                                AddTerminalRequest(
                                    terminalName,
                                    BuildConfig.COMFYGROUPTRACKSERVICEID.toLong()), object: OnTrackListener {
                                    override fun onLatestPointCallback(p0: LatestPointResponse?) {}
                                    override fun onCreateTerminalCallback(p0: AddTerminalResponse?) {
                                        if (p0 != null && p0.isSuccess) {
                                            // 创建完成，开启猎鹰服务
                                            terminalId = p0.tid
//                                            view.amapTrackClient.startTrack(
//                                                TrackParam(BuildConfig.COMFYGROUPTRACKSERVICEID.toLong(), terminalId),
//                                                onTrackLifecycleListener
                                            view.amapTrackClient.addTrack(
                                                AddTrackRequest(serviceId, terminalId), object: OnTrackListener{
                                                    override fun onLatestPointCallback(p0: LatestPointResponse?) {}
                                                    override fun onCreateTerminalCallback(p0: AddTerminalResponse?) {}
                                                    override fun onQueryTrackCallback(p0: QueryTrackResponse?) {}
                                                    override fun onDistanceCallback(p0: DistanceResponse?) {}
                                                    override fun onQueryTerminalCallback(p0: QueryTerminalResponse?) {}
                                                    override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {}
                                                    override fun onParamErrorCallback(p0: ParamErrorResponse?) {}
                                                    override fun onAddTrackCallback(p0: AddTrackResponse?) {
                                                        if(p0 != null && p0.isSuccess){
                                                            trackId = p0.trid
                                                            startUploadTask()
                                                        }
                                                        else{
                                                            view.yumLayout.yum("网络请求失败" + p0?.errorMsg).show()
                                                        }
                                                    }
                                                }
                                            )
                                        } else {
                                            // 请求失败
                                            view.yumLayout.yum("请求失败.").show()
                                        }
                                    }
                                    override fun onQueryTrackCallback(p0: QueryTrackResponse?) {}
                                    override fun onDistanceCallback(p0: DistanceResponse?) {}
                                    override fun onQueryTerminalCallback(p0: QueryTerminalResponse?) {}
                                    override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {}
                                    override fun onParamErrorCallback(p0: ParamErrorResponse?) {}
                                    override fun onAddTrackCallback(p0: AddTrackResponse?) {}
                                })
                        }
                        else{
                            val lastTerminalId = p0.tid
                            terminalId = lastTerminalId
                            view.amapTrackClient.addTrack(
                                AddTrackRequest(serviceId, terminalId), object: OnTrackListener{
                                    override fun onLatestPointCallback(p0: LatestPointResponse?) {}
                                    override fun onCreateTerminalCallback(p0: AddTerminalResponse?) {}
                                    override fun onQueryTrackCallback(p0: QueryTrackResponse?) {}
                                    override fun onDistanceCallback(p0: DistanceResponse?) {}
                                    override fun onQueryTerminalCallback(p0: QueryTerminalResponse?) {}
                                    override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {}
                                    override fun onParamErrorCallback(p0: ParamErrorResponse?) {}
                                    override fun onAddTrackCallback(p0: AddTrackResponse?) {
                                        if(p0 != null && p0.isSuccess){
                                            trackId = p0.trid
                                            startUploadTask()
                                        }
                                        else{
                                            view.yumLayout.yum("网络请求失败" + p0?.errorMsg).show()
                                        }
                                    }
                                }
                            )
                        }
                    }
                    else{
                        //请求失败
                        view.yumLayout.yum("请求失败，" + p0!!.errorMsg).show();
                    }
                }
                override fun onHistoryTrackCallback(p0: HistoryTrackResponse?) {}
                override fun onParamErrorCallback(p0: ParamErrorResponse?) {}
                override fun onAddTrackCallback(p0: AddTrackResponse?) {
                    if (p0 != null && p0.isSuccess) {
                        trackId = p0.trid
                        val trackParam = TrackParam(BuildConfig.COMFYGROUPTRACKSERVICEID.toLong(), terminalId)
                        trackParam.trackId = trackId
                        view.amapTrackClient.startTrack(trackParam, onTrackLifecycleListener)
                    } else {
                        view.yumLayout.yum("网络请求失败，" +
                                p0?.errorMsg).show()
                    }
                }

            })
    }

    private fun startUploadTask(){
        val query = AVQuery<AVObject>("GroupTrack")
        query.whereEqualTo("userId", terminalName)
        query.findInBackground(object : FindCallback<AVObject>(){
            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                if(p0 == null || p0.size == 0){
                    val newInfo = AVObject("GroupTrack")
                    newInfo.put("userId", terminalName)
                    newInfo.put("terminalId", terminalId)
                    newInfo.put("trackId", trackId)
                    newInfo.saveInBackground(object : SaveCallback(){
                        override fun done(p0: AVException?) {
                            if(p0 == null){
                                val trackParam = TrackParam(serviceId, terminalId)
                                trackParam.trackId = trackId
                                view.amapTrackClient.startTrack(trackParam, onTrackLifecycleListener)
                                fetchDownloadPairs()
                            }
                            else{
                                view.onServiceBroken()
                            }
                        }
                    })
                }
                else{
                    val previous = p0[0]
                    previous.put("trackId", trackId)
                    previous.saveInBackground(object : SaveCallback(){
                        override fun done(p0: AVException?) {
                            if(p0 == null){
                                val trackParam = TrackParam(serviceId, terminalId)
                                trackParam.trackId = trackId
                                view.amapTrackClient.startTrack(trackParam, onTrackLifecycleListener)
                                fetchDownloadPairs()
                            }
                            else{
                                view.onServiceBroken()
                            }
                        }
                    })
                }
            }
        })
    }

    fun startDownloadAsyncTask(){
//        groupTrackAsyncTask = DownloadPointAsyncTask(view)
//        groupTrackAsyncTask.execute(downloadPairs)
        groupTrackTimerTask = DownloadPointTimer(downloadPairs, view)
        timer = Timer()
        timer.schedule(groupTrackTimerTask, 0, 5000)
    }

    private fun fetchDownloadPairs(){
        val queryList : MutableList<AVQuery<AVObject>> = mutableListOf()
        for(id in view.trackList){
            val newQuery = AVQuery<AVObject>("GroupTrack")
            newQuery.whereEqualTo("userId", id)
            queryList.add(newQuery)
        }
        val ultimateQuery = AVQuery.or(queryList)
        ultimateQuery.findInBackground(object: FindCallback<AVObject>(){
            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                if(p0 != null && p0.size > 0){
                    for (target in p0){
                        val newPair = Pair(target.getLong("terminalId"),
                            target.getLong("trackId"))
                        downloadPairs.add(newPair)
                        startDownloadAsyncTask()
                    }
                }
            }
        })
    }

    fun onViewDestroyed(){
//        groupTrackAsyncTask.cancel(true)
        timer.cancel()
    }

}