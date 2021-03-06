package com.kha.cbc.comfy.presenter;

import android.app.Application;
import android.widget.Toast;
import com.avos.avoscloud.*;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.entity.GDPersonalCard;
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao;
import com.kha.cbc.comfy.model.PersonalCard;
import com.kha.cbc.comfy.model.TeamCard;
import com.kha.cbc.comfy.presenter.Notification.CloudPushHelper;
import com.kha.cbc.comfy.view.common.AvatarView;
import com.kha.cbc.comfy.view.plus.PlusCardActivity;
import com.kha.cbc.comfy.view.plus.PlusCardView;

import java.util.ArrayList;
import java.util.List;

public class PlusCardPresenter extends AvatarPresenter {

    PlusCardView plusCardView;

    public PlusCardPresenter(PlusCardView plusCardView) {
        super((AvatarView) plusCardView);
        this.plusCardView = plusCardView;
    }

    //将新增卡片信息上传至服务器
    public void postCard(String title,
                         String description,
                         String executorObjectId,
                         String stageObjectId,
                         String taskObjectId) {
        AVObject card = new AVObject("TeamCard");
        AVObject user = AVObject.createWithoutData("ComfyUser",
                executorObjectId);
        card.put("Executor", user);
        AVObject stage = AVObject.createWithoutData("Stage",
                stageObjectId);
        card.put("Stage", stage);
        card.put("CardTitle", title);
        card.put("Description", description);
        card.saveInBackground();
        pushMap(taskObjectId, executorObjectId, title);
    }

    public void pullCard(String cardObjectId) {
        AVQuery<AVObject> query = new AVQuery<>("TeamCard");
        query.include("Stage");
        query.include("Executor");
        query.getInBackground(cardObjectId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                TeamCard card = new TeamCard(avObject.getAVObject("Stage").getString("TaskId"),
                        avObject.getAVObject("Executor").getString("username"),
                        avObject.getString("CardTitle"),
                        avObject.getObjectId(),
                        avObject.getString("Description"));
                plusCardView.setSavedCard(card);
            }
        });
    }

    public PersonalCard getLocalPersonalCard (String id, Application application) {
        GDPersonalCardDao cardDao = ((ComfyApp) application).getDaoSession().getGDPersonalCardDao();
        GDPersonalCard card = cardDao.queryBuilder().where(GDPersonalCardDao.Properties.Id.eq(id)).unique();
        return new PersonalCard(card);
    }

//    public void setCloudReminder(LinearLayout linearLayout) {
//
//    }

    public void queryMember(String memberName) {
        List<String> nameList = new ArrayList<>();
        nameList.add(memberName);
        AVQuery<AVObject> query = new AVQuery<>("ComfyUser");
        query.whereEqualTo("username", memberName);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (!list.isEmpty())
                    ((PlusCardActivity) plusCardView).setExecutor(list.get(0).getObjectId());
                else
                    Toast.makeText((PlusCardActivity)plusCardView, "没有该用户", Toast.LENGTH_SHORT);
            }
        });
        super.loadAvatar(nameList);
    }

    public void pushMap(String taskObjectId, String executorObjectId, String title) {
        AVObject task = AVObject.createWithoutData("TeamTask", taskObjectId);
        AVObject user = AVObject.createWithoutData("ComfyUser", executorObjectId);
        AVQuery<AVObject> queryMap = new AVQuery<>("UserTaskMap");
        AVObject teamTask = AVObject.createWithoutData("TeamTask", taskObjectId);
        queryMap.whereEqualTo("TeamTask", teamTask);
        queryMap.include("Member");
        queryMap.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                boolean notFound = true;
                for (AVObject object : list) {
                    if (object.getAVObject("Member").equals(user)) {
                        notFound = false;
                        break;
                    }
                }
                if (notFound) {
                    AVObject map = new AVObject("UserTaskMap");
                    map.put("TeamTask", teamTask);
                    map.put("Member", user);
                    map.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            CloudPushHelper.pushInvitation(taskObjectId, executorObjectId, title);
                        }
                    });
                } else {
                    CloudPushHelper.pushInvitation(taskObjectId, executorObjectId, title);
                }
            }
        });
    }
}
