package com.kha.cbc.comfy.presenter.Notification;

import android.util.Log;
import android.widget.Toast;
import com.avos.avoscloud.*;
import com.kha.cbc.comfy.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/12/21
 */
public class CloudPushHelper {

    public static void pushInvitation(String taskObjectId, String newMemberId, String cardTitle) {
        AVQuery<AVObject> query = new AVQuery<>("UserTaskMap");
        AVObject teamTask = AVObject.createWithoutData("TeamTask", taskObjectId);
        query.include("Member");
        query.whereEqualTo("TeamTask", teamTask);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject map : list) {
                    AVObject member = map.getAVObject("Member");
                    if (member.getObjectId().equals(User.INSTANCE.getComfyUserObjectId())) {
                        Log.d("message1", "message1");
                    } else if (member.getObjectId().equals(newMemberId)) {
                        Log.d("message2", "message2");
                        AVPush push = new AVPush();
                        JSONObject data = new JSONObject();
                        try {
                            data.put("action", "com.kha.cbc.comfy.NEWADDMESSAGE");
                            data.put("alert", cardTitle);
                        }
                        catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        push.setData(data);
                        AVQuery pushQuery = AVInstallation.getQuery();
                        pushQuery.whereEqualTo("installationId", member.getString("InstallationId"));
                        push.setQuery(pushQuery);
                        push.sendInBackground();
                    } else {
                        Log.d("message3", "message3");
                        AVPush push = new AVPush();
                        JSONObject data = new JSONObject();
                        try {
                            data.put("action", "com.kha.cbc.comfy.NEWASSIGNMESSAGE");
                            data.put("alert", cardTitle);
                        }
                        catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        push.setData(data);
                        AVQuery pushQuery = AVInstallation.getQuery();
                        pushQuery.whereEqualTo("installationId", member.getString("InstallationId"));
                        push.setQuery(pushQuery);
                        push.sendInBackground();
                    }
                }
            }
        });
    }
}
