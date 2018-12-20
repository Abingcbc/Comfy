package com.kha.cbc.comfy;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import cn.leancloud.chatkit.LCChatKit;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.kha.cbc.comfy.data.network.providers.CustomUserProvider;
import com.kha.cbc.comfy.greendao.gen.DaoMaster;
import com.kha.cbc.comfy.greendao.gen.DaoSession;

public class ComfyApp extends Application {

    DaoSession daoSession;

    //LeanCloud
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, BuildConfig.LEANCLOUDAPPID, BuildConfig.LEANCLOUDAPPKEY);

        AVInstallation.getCurrentInstallation().saveInBackground();

        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.Companion.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), BuildConfig.LEANCLOUDAPPID,
                BuildConfig.LEANCLOUDAPPKEY);

        //LeanCloud debug log
        AVOSCloud.setDebugLogEnabled(true);

        //初始化本地数据库
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "comfy.db");
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
