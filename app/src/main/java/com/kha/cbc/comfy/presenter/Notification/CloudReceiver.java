package com.kha.cbc.comfy.presenter.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class CloudReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            JSONObject jsonObject = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
            String action = jsonObject.getString("action");
            Intent intentNotify = new Intent(context, CloudIntentService.class);
            if (jsonObject.getBoolean("hasTitle")) {
                String title = jsonObject.getString("title");
                intentNotify.putExtra("title", title);
            }
            String taskTitle = jsonObject.getString("taskTitle");
            intentNotify.putExtra("taskTitle", taskTitle);
            intentNotify.putExtra("action", action);
            context.startService(intentNotify);
            Log.d(action, taskTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
