package com.kha.cbc.comfy;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;

public class ComfyApp extends Application {

    //LeanCloud
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, BuildConfig.LEANCLOUDAPPID, BuildConfig.LEANCLOUDAPPKEY);

        //leancloud debuglog
        AVOSCloud.setDebugLogEnabled(true);
    }
}
