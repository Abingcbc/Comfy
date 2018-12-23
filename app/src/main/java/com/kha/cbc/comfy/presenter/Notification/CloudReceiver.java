package com.kha.cbc.comfy.presenter.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by ABINGCBC
 * on 2018/12/20
 */
public class CloudReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            JSONObject jsonObject = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
            String cardTitle = jsonObject.getString("cardTitle");
            String taskTitle = jsonObject.getString("taskTitle");
            Intent intentNotify = new Intent(context, CloudIntentService.class);
            intentNotify.putExtra("cardTitle", cardTitle);
            intentNotify.putExtra("taskTitle", taskTitle);
            intentNotify.putExtra("action", jsonObject.getString("action"));
            context.startService(intentNotify);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
