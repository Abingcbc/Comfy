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
    static final String UPDATECARDMESSAGE = "com.kha.cbc.comfy.UPDATECARDMESSAGE";
    static final String COMPLETECARDMESSAGE = "com.kha.cbc.comfy.COMPLETECARDMESSAGE";

    static final String DELETESTAGEMESSAGE = "com.kha.cbc.comfy.DELETESTAGEMESSAGE";
    static final String UPDATESTAGEMESSAGE = "com.kha.cbc.comfy.UPDATESTAGEMESSAGE";
    static final String COMPLETESTAGEMESSAGE = "com.kha.cbc.comfy.COMPLETESTAGEMESSAGE";

    static final String DELETETASKMESSAGE = "com.kha.cbc.comfy.DELETETASKMESSAGE";
    static final String UPDATETASKMESSAGE = "com.kha.cbc.comfy.UPDATETASKMESSAGE";
    static final String COMPLETETASKMESSAGE = "com.kha.cbc.comfy.COMPLETETASKMESSAGE";

    public CloudIntentService() {
        super("CloudIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
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
                        title = intent.getStringExtra("title");
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                            .setContentText("您在项目 " + taskTitle + " 中" + ASSIGN + title + COMMON)
                            .setSmallIcon(R.drawable.default_avatar)
                            .setChannelId(CHANNEL_ID)
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                            .setContentIntent(pi)
                            .build();
                        break;
                    }
                    case ADDMESSAGE: {
                        title = intent.getStringExtra("title");
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                            .setContentText(ADDFRONT + title + ADD + taskTitle + COMMON)
                            .setSmallIcon(R.drawable.default_avatar)
                            .setChannelId(CHANNEL_ID)
                            .setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                            .setContentIntent(pi)
                            .build();
                        break;
                    }
                    case DELETECARDMESSAGE: {
                        title = intent.getStringExtra("title");
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 中的任务 " + title + "被取消了" + COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        break;
                    }
                    case COMPLETECARDMESSAGE: {
                        title = intent.getStringExtra("title");
                        taskTitle = intent.getStringExtra("taskTitle");
                        Log.d(COMPLETECARDMESSAGE, title);
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 中的任务 " + title + "完成了" + COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        break;
                    }
                    case UPDATECARDMESSAGE: {
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 中有任务更新了" + COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        break;
                    }
                    case DELETESTAGEMESSAGE: {
                        title = intent.getStringExtra("title");
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 中的任务列表 " +
                                        title + " 被删除了"+ COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        break;
                    }
                    case COMPLETESTAGEMESSAGE: {
                        title = intent.getStringExtra("title");
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 中的任务列表 " +
                                        title + " 完成了"+ COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        break;
                    }
                    case UPDATESTAGEMESSAGE: {
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 中的有任务列表更新了"+ COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        break;
                    }
                    case DELETETASKMESSAGE: {
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 删除了"+ COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        break;
                    }
                    case COMPLETETASKMESSAGE: {
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 完成了"+ COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        break;
                    }
                    case UPDATETASKMESSAGE: {
                        taskTitle = intent.getStringExtra("taskTitle");
                        notification = builder
                                .setContentText("您项目 " + taskTitle + " 更新了"+ COMMON)
                                .setSmallIcon(R.drawable.default_avatar)
                                .setChannelId(CHANNEL_ID)
                                .setAutoCancel(true)
                                .setWhen(System.currentTimeMillis())
                                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.default_avatar))
                                .setContentIntent(pi)
                                .build();
                        break;
                    }
                }
                NotificationManager mNotificationManager =
                        (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.createNotificationChannel(mChannel);
                if (title != null)
                    mNotificationManager.notify(title.hashCode(), notification);
                else
                    mNotificationManager.notify(taskTitle.hashCode(), notification);
            }
        }
    }
}
