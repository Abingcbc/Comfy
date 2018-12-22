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

    String title;
    Boolean isAssign;

    static final String HEADER = "团队项目变更提醒";
    static final String ASSIGN = "您被分配了新的任务 ";
    static final String ADD = "有新的项目加入了您的项目 ";
    static final String COMMON = " 快去看看吧";

    public CloudIntentService() {
        super("CloudIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            title = intent.getStringExtra("title");
            isAssign = intent.getBooleanExtra("isAssign", true);
            Intent intentMain = new Intent(this, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intentMain, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setTicker(HEADER).setContentTitle(HEADER);
            String CHANNEL_ID = "comfy_cloud_channel";// The id of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            Log.d("point4", "point4");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
                if (isAssign) {
                    Notification notification = builder
                            .setContentText(ASSIGN + title + COMMON)
                            .setSmallIcon(R.drawable.default_avatar)
                            .setChannelId(CHANNEL_ID)
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                            .setContentIntent(pi)
                            .build();
                    NotificationManager mNotificationManager =
                            (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.createNotificationChannel(mChannel);
                    mNotificationManager.notify(title.hashCode(), notification);
                    Log.d("point5", "point5");
                } else {
                    Notification notification = builder
                            .setContentText(ADD + title + COMMON)
                            .setSmallIcon(R.drawable.default_avatar)
                            .setChannelId(CHANNEL_ID)
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                            .setContentIntent(pi)
                            .build();
                    Log.d("point6", "point6");
                    NotificationManager mNotificationManager =
                            (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.createNotificationChannel(mChannel);
                    mNotificationManager.notify(title.hashCode(), notification);
                }
            }
        }
    }
}
