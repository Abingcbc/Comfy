package com.kha.cbc.comfy.entity;

import com.kha.cbc.comfy.model.common.BaseCardModel;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ABINGCBC
 * on 2018/11/5
 */

@Entity
public class GDPersonalCard {

    String title;
    String description;
    String taskId;

    GDPersonalCard(BaseCardModel cardModel) {
        this.title = cardModel.getTitle();
        this.description = cardModel.getDescription();
        this.taskId = cardModel.getTaskId();
    }

    @Generated(hash = 929999000)
    public GDPersonalCard(String title, String description, String taskId) {
        this.title = title;
        this.description = description;
        this.taskId = taskId;
    }

    @Generated(hash = 806343092)
    public GDPersonalCard() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
