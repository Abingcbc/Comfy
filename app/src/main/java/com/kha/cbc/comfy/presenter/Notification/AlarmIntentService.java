package com.kha.cbc.comfy.presenter.Notification;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import androidx.core.app.NotificationCompat;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.entity.GDPersonalCard;
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao;
import com.kha.cbc.comfy.view.main.MainActivity;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class AlarmIntentService extends IntentService {

    static final String PERSONAL_TASK = "个人日程提醒";
    static final String TEAM_TASK = "团队日程提醒";
    boolean isPersonal;
    String cardTitle;
    String id;
    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            isPersonal = intent.getBooleanExtra("isPersonal", false);
            id = intent.getStringExtra("id");
            if (isPersonal) {
                GDPersonalCardDao personalCardDao = ((ComfyApp) getApplication()).
                        getDaoSession().getGDPersonalCardDao();
                GDPersonalCard card = personalCardDao.queryBuilder().
                        where(GDPersonalCardDao.Properties.Id.eq(id)).unique();
                if (card.getIsRemind()) {
                    cardTitle = card.getTitle();
                    card.setIsRemind(false);
                    personalCardDao.update(card);
                    show();
                }
            }
        }
    }
    void show() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (isPersonal)
            builder.setTicker(PERSONAL_TASK)
                    .setContentTitle(PERSONAL_TASK);
        else
            builder.setTicker(TEAM_TASK)
                    .setContentTitle(TEAM_TASK);
        String CHANNEL_ID = "comfy_alarm_channel";// The id of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
            Notification notification = builder
                    .setContentText("任务完成提醒 " + cardTitle)
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
            mNotificationManager.notify(id.hashCode(), notification);
        }
    }
}
