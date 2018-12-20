package com.kha.cbc.comfy.presenter;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import com.avos.avoscloud.*;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.entity.GDPersonalCard;
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao;
import com.kha.cbc.comfy.model.PersonalCard;
import com.kha.cbc.comfy.view.common.AvatarView;
import com.kha.cbc.comfy.view.plus.PlusCardActivity;
import com.kha.cbc.comfy.view.plus.PlusCardView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/5
 */

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
        AVObject map = new AVObject("UserTaskMap");
        AVObject teamTask = AVObject.createWithoutData("TeamTask", taskObjectId);
        map.put("TeamTask", teamTask);
        map.put("Member", user);
        map.saveInBackground();
    }

    public PersonalCard getLocalPersonalCard (String id, Application application) {
        GDPersonalCardDao cardDao = ((ComfyApp) application).getDaoSession().getGDPersonalCardDao();
        GDPersonalCard card = cardDao.queryBuilder().where(GDPersonalCardDao.Properties.Id.eq(id)).unique();
        return new PersonalCard(card);
    }

    public void setCloudReminder(LinearLayout linearLayout) {

    }

    public void queryMember(String memberName) {
        List<String> nameList = new ArrayList<>();
        nameList.add(memberName);
        super.loadAvatar(nameList);
    }
}
