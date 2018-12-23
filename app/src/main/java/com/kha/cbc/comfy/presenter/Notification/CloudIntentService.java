package com.kha.cbc.comfy.presenter.Notification;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.view.main.MainActivity;


public class CloudIntentService extends IntentService {

    String cardTitle;
    String taskTitle;
    String action;

    static final String HEADER = "团队项目变更提醒";
    static final String ASSIGN = " 被分配了新的任务 ";
    static final String ADDFRONT = "有新的任务 ";
    static final String ADD = " 加入了您的项目 ";
    static final String COMMON = " 快去看看吧";

    static final String ASSIGNMESSAGE = "com.kha.cbc.comfy.NEWASSIGNMESSAGE";
    static final String ADDMESSAGE = "com.kha.cbc.comfy.NEWADDMESSAGE";
    static final String DELETECARDMESSAGE = "com.kha.cbc.comfy.DELETECARDMESSAGE";
    static final String DELETETASKMESSAGE = "com.kha.cbc.comfy.DELETETASKMESSAGE";
    static final String COMPELTECARDMESSAGE = "com.kha.cbc.comfy.COMPELTECARDMESSAGE";
    static final String UPDATEMESSAFE = "com.kha.cbc.comfy.UPDATEMESSAGE";

    public CloudIntentService() {
        super("CloudIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            cardTitle = intent.getStringExtra("cardTitle");
            taskTitle = intent.getStringExtra("taskTitle");
            action = intent.getStringExtra("action");

            Intent intentMain = new Intent(this, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intentMain, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setTicker(HEADER).setContentTitle(HEADER);
            String CHANNEL_ID = "comfy_cloud_channel";// The id of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
                Notification notification = new Notification();
                switch (action) {
                    case ASSIGNMESSAGE: {
                        notification = builder
                            .setContentText("您在项目 " + taskTitle + " 中" + ASSIGN + cardTitle + COMMON)
                            .setSmallIcon(R.drawable.default_avatar)
                            .setChannelId(CHANNEL_ID)
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                            .setContentIntent(pi)
                            .build();
                        Log.d("assign notification", cardTitle);
                        break;
                    }
                    case ADDMESSAGE: {
                        notification = builder
                            .setContentText(ADDFRONT + cardTitle + ADD + taskTitle + COMMON)
                            .setSmallIcon(R.drawable.default_avatar)
                            .setChannelId(CHANNEL_ID)
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                            .setContentIntent(pi)
                            .build();
                        Log.d("add notification", cardTitle);
                        break;
                    }
                    case DELETECARDMESSAGE: {
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 中的任务 " + cardTitle + "被取消了" + COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        Log.d("delete notification", cardTitle);
                        break;
                    }
                    case COMPELTECARDMESSAGE: {
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 中的任务 " + cardTitle + "完成了" + COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        Log.d("complete notification", cardTitle);
                        break;
                    }
                    case UPDATEMESSAFE: {
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 中有任务更新了" + COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        Log.d("update notification", cardTitle);
                        break;
                    }
                }
                NotificationManager mNotificationManager =
                        (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager.notify(cardTitle.hashCode(), notification);
            }
        }
    }
}
