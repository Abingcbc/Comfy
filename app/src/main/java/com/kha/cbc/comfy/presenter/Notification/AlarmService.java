package com.kha.cbc.comfy.presenter.Notification;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

/**
 * Created by ABINGCBC
 * on 2018/12/6
 */
public class AlarmService extends IntentService {

    public AlarmService() {
        super("taskAlarm");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
