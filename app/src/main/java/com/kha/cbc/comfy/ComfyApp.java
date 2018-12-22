package com.kha.cbc.comfy;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import cn.leancloud.chatkit.LCChatKit;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.bilibili.magicasakura.utils.ThemeUtils;
import com.kha.cbc.comfy.data.network.providers.CustomUserProvider;
import com.kha.cbc.comfy.greendao.gen.DaoMaster;
import com.kha.cbc.comfy.greendao.gen.DaoSession;
import com.kha.cbc.comfy.view.common.ThemeHelper;

public class ComfyApp extends Application implements ThemeUtils.switchColor {

    DaoSession daoSession;


    private
    @ColorRes
    int getThemeColorId(Context context, int colorId, String theme) {
        switch (colorId) {
            case R.color.theme_color_primary:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case R.color.theme_color_primary_dark:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case R.color.theme_color_primary_trans:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
        }
        return colorId;
    }

    @Override
    public int replaceColorById(Context context, int colorId) {
        if (ThemeHelper.INSTANCE.isDefaultTheme(context)) {
            return ContextCompat.getColor(context, colorId);
        }
        String theme = getTheme(context);
        if (theme != null) {
            colorId = getThemeColorId(context, colorId, theme);
        }
        return ContextCompat.getColor(context, colorId);
    }

    private String getTheme(Context context) {
        if (ThemeHelper.INSTANCE.getTheme(context) == ThemeHelper.INSTANCE.getCARD_STORM()) {
            return "blue";
        } else if (ThemeHelper.INSTANCE.getTheme(context) == ThemeHelper.INSTANCE.getCARD_HOPE()) {
            return "purple";
        } else if (ThemeHelper.INSTANCE.getTheme(context) == ThemeHelper.INSTANCE.getCARD_WOOD()) {
            return "green";
        } else if (ThemeHelper.INSTANCE.getTheme(context) == ThemeHelper.INSTANCE.getCARD_LIGHT()) {
            return "green_light";
        } else if (ThemeHelper.INSTANCE.getTheme(context) == ThemeHelper.INSTANCE.getCARD_THUNDER()) {
            return "yellow";
        } else if (ThemeHelper.INSTANCE.getTheme(context) == ThemeHelper.INSTANCE.getCARD_SAND()) {
            return "orange";
        } else if (ThemeHelper.INSTANCE.getTheme(context) == ThemeHelper.INSTANCE.getCARD_FIREY()) {
            return "red";
        } else if (ThemeHelper.INSTANCE.getTheme(context) == ThemeHelper.INSTANCE.getCARD_DARK()){
            return "dark";
        }
        return null;
    }

    private
    @ColorRes
    int getThemeColor(Context context, int color, String theme) {
        switch (color) {
            case 0xfffb7299:
                return context.getResources().getIdentifier(theme, "color", getPackageName());
            case 0xffb85671:
                return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
            case 0x99f0486c:
                return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
        }
        return -1;
    }

    @Override
    public int replaceColor(Context context, int color) {
        if (ThemeHelper.INSTANCE.isDefaultTheme(context)) {
            return color;
        }
        String theme = getTheme(context);
        int colorId = -1;

        if (theme != null) {
            colorId = getThemeColor(context, color, theme);
        }
        return colorId != -1 ? ContextCompat.getColor(context, colorId) : color;
    }

    //LeanCloud
    @Override
    public void onCreate() {
        super.onCreate();
        ThemeUtils.setSwitchColor(this);

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, BuildConfig.LEANCLOUDAPPID, BuildConfig.LEANCLOUDAPPKEY);


        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.Companion.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), BuildConfig.LEANCLOUDAPPID,
                BuildConfig.LEANCLOUDAPPKEY);

        //LeanCloud debug log
        AVOSCloud.setDebugLogEnabled(true);

        //初始化本地数据库
        initGreenDao();


        AVInstallation.getCurrentInstallation().saveInBackground();
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
