package com.kha.cbc.comfy.presenter.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import androidx.core.app.NotificationCompat;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.view.main.MainActivity;

/**
 * Created by ABINGCBC
 * on 2018/12/6
 */
public class AlarmReceiver extends BroadcastReceiver {

    final static String PERSONAL_TASK = "个人日程提醒";
    final static String TEAM_TASK = "团队日程提醒";

    Boolean isPersonal;
    String taskTitle;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        isPersonal = bundle.getBoolean("isPersonal");
        taskTitle = bundle.getString("taskTitle");
        show(context);
    }

    void show(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        if (isPersonal)
            builder.setTicker(PERSONAL_TASK)
                    .setContentTitle(PERSONAL_TASK);
        else
            builder.setTicker(TEAM_TASK)
                    .setContentTitle(TEAM_TASK);
        Notification notification = builder.setContentText("完成任务提醒：" + taskTitle)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pi)
                .build();
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify("Notification", 1, notification);
    }
}
