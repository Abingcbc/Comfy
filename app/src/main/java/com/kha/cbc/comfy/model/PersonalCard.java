package com.kha.cbc.comfy.model;

import com.kha.cbc.comfy.entity.GDPersonalCard;
import com.kha.cbc.comfy.model.common.BaseCardModel;

public class PersonalCard extends BaseCardModel {

    public PersonalCard(String title, String description, String taskId) {
        super(taskId);
        this.title = title;
        this.description = description;
        this.isRemind = false;
        this.id = initId(title);
    }

    public PersonalCard(String title, String description, String taskId, String remindDate) {
        super(taskId);
        this.title = title;
        this.description = description;
        this.remindDate = remindDate;
        this.isRemind = true;
        this.id = initId(title);
    }

    public PersonalCard(GDPersonalCard card) {
        super(card.getTaskId());
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.remindDate = card.getRemindDate();
        this.isRemind = card.getIsRemind();
        this.id = card.getId();
    }
}
