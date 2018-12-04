package com.kha.cbc.comfy.entity;

import com.kha.cbc.comfy.model.common.BaseCardModel;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by ABINGCBC
 * on 2018/11/5
 */

@Entity
public class GDPersonalCard {

    @Id
    String id;
    String title;
    String description;
    String taskId;

    public GDPersonalCard(BaseCardModel cardModel) {
        this.title = cardModel.getTitle();
        this.description = cardModel.getDescription();
        this.taskId = cardModel.getTaskId();
        this.id = title + taskId;
    }


    public GDPersonalCard(String title, String description,
                          String taskId) {
        this.id = title + taskId;
        this.title = title;
        this.description = description;
        this.taskId = taskId;
    }

    @Generated(hash = 1897112959)
    public GDPersonalCard(String id, String title, String description,
            String taskId) {
        this.id = id;
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

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
