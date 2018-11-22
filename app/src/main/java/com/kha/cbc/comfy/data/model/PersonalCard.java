package com.kha.cbc.comfy.data.model;

import com.kha.cbc.comfy.common.BaseCardModel;
import com.kha.cbc.comfy.data.entity.GDPersonalCard;

/**
 * Created by ABINGCBC
 * on 2018/11/2
 */

public class PersonalCard extends BaseCardModel {

    public PersonalCard(String title, String description, String taskId) {
        super(taskId);
        this.title = title;
        this.description = description;
    }
}
