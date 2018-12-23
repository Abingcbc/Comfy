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

    public static void pushInvitation(String taskObjectId, String newMemberId,
                                      String cardTitle) {
        AVQuery<AVObject> query = new AVQuery<>("UserTaskMap");
        AVObject teamTask = AVObject.createWithoutData("TeamTask", taskObjectId);
        query.include("Member");
        query.include("TeamTask");
        query.whereEqualTo("TeamTask", teamTask);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject map : list) {
                    AVObject member = map.getAVObject("Member");
                    if (!member.getObjectId().equals(User.INSTANCE.getComfyUserObjectId())) {
                        JSONObject data = new JSONObject();
                        if (member.getObjectId().equals(newMemberId)) {
                            try {
                                data.put("action", CloudIntentService.ASSIGNMESSAGE);
                                data.put("cardTitle", cardTitle);
                                data.put("taskTitle", map.getAVObject("TeamTask").getString("TaskTitle"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            try {
                                data.put("action", CloudIntentService.ADDMESSAGE);
                                data.put("cardTitle", cardTitle);
                                data.put("taskTitle", map.getAVObject("TeamTask").getString("TaskTitle"));
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                        AVQuery<AVObject> queryInstallation = new AVQuery<>("UserInstallationMap");
                        queryInstallation.include("InstallationId");
                        queryInstallation.whereEqualTo("User", member);
                        queryInstallation.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                for (AVObject avObject : list) {
                                    AVPush push = new AVPush();
                                    push.setData(data);
                                    AVQuery pushQuery = AVInstallation.getQuery();
                                    pushQuery.whereEqualTo("installationId",
                                            avObject.getString("InstallationId"));
                                    push.setQuery(pushQuery);
                                    push.sendInBackground();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public static void pushOperation(String taskObjectId, String cardTitle, Boolean isCompleted) {
        AVQuery<AVObject> query = new AVQuery<>("UserTaskMap");
        AVObject teamTask = AVObject.createWithoutData("TeamTask", taskObjectId);
        query.include("Member");
        query.include("TeamTask");
        query.whereEqualTo("TeamTask", teamTask);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject map : list) {
                    AVObject member = map.getAVObject("Member");
                    if (member.getObjectId().equals(User.INSTANCE.getComfyUserObjectId())) {
                    } else {
                        JSONObject data = new JSONObject();
                        try {
                            if (isCompleted)
                                data.put("action", CloudIntentService.COMPELTECARDMESSAGE);
                            else
                                data.put("action", CloudIntentService.DELETECARDMESSAGE);
                            data.put("cardTitle", cardTitle);
                            data.put("taskTitle", map.getAVObject("TeamTask").getString("TaskTitle"));
                        }
                        catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        AVQuery<AVObject> queryInstallation = new AVQuery<>("UserInstallationMap");
                        queryInstallation.include("InstallationId");
                        queryInstallation.whereEqualTo("User", member);
                        queryInstallation.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                for (AVObject avObject : list) {
                                    AVPush push = new AVPush();
                                    push.setData(data);
                                    AVQuery pushQuery = AVInstallation.getQuery();
                                    pushQuery.whereEqualTo("installationId",
                                            avObject.getString("InstallationId"));
                                    push.setQuery(pushQuery);
                                    push.sendInBackground();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public static void pushUpdate(String taskObjectId) {
        AVQuery<AVObject> query = new AVQuery<>("UserTaskMap");
        AVObject teamTask = AVObject.createWithoutData("TeamTask", taskObjectId);
        query.include("Member");
        query.include("TeamTask");
        query.whereEqualTo("TeamTask", teamTask);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject map : list) {
                    AVObject member = map.getAVObject("Member");
                    if (member.getObjectId().equals(User.INSTANCE.getComfyUserObjectId())) {
                    } else {
                        JSONObject data = new JSONObject();
                        try {
                            data.put("action", CloudIntentService.UPDATEMESSAFE);
                            data.put("taskTitle", map.getAVObject("TeamTask").getString("TaskTitle"));
                        }
                        catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        AVQuery<AVObject> queryInstallation = new AVQuery<>("UserInstallationMap");
                        queryInstallation.include("InstallationId");
                        queryInstallation.whereEqualTo("User", member);
                        queryInstallation.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                for (AVObject avObject : list) {
                                    AVPush push = new AVPush();
                                    push.setData(data);
                                    AVQuery pushQuery = AVInstallation.getQuery();
                                    pushQuery.whereEqualTo("installationId",
                                            avObject.getString("InstallationId"));
                                    push.setQuery(pushQuery);
                                    push.sendInBackground();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
