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
            String message = jsonObject.getString("alert");
            Log.d("point1", "point1");
            if (intent.getAction().equals("com.kha.cbc.comfy.NEWASSIGNMESSAGE")) {
                Intent intentNotify = new Intent(context, CloudIntentService.class);
                intentNotify.putExtra("title", message);
                intentNotify.putExtra("isAssign", true);
                Log.d("point2", "point2");
                context.startService(intentNotify);
            } else if (intent.getAction().equals("com.kha.cbc.comfy.NEWADDMESSAGE")) {
                Intent intentNotify = new Intent(context, CloudIntentService.class);
                intentNotify.putExtra("title", message);
                intentNotify.putExtra("isAssign", false);
                Log.d("point3", "point3");
                context.startService(intentNotify);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
