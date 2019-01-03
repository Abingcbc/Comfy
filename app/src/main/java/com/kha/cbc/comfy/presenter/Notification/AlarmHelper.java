package com.kha.cbc.comfy.presenter.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.kha.cbc.comfy.entity.GDPersonalCard;

import java.util.Date;

public class AlarmHelper {

    static public void deleteLocalReminder(GDPersonalCard card) {
        card.setIsRemind(false);
    }

    static public void updateLocalReminder(GDPersonalCard card, Context context,
                                           Date reminderDate) {
        getNewLocalReminder(card, context, reminderDate);
    }

    static public void getNewLocalReminder(GDPersonalCard card, Context context,
                                           Date reminderDate) {
        Intent intent = new Intent(context, AlarmIntentService.class);
        intent.putExtra("isPersonal", true);
        intent.putExtra("id", card.getId());
        PendingIntent sender = PendingIntent.
                getService(context,
                        card.getId().hashCode(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminderDate.getTime(),sender);
    }
}
