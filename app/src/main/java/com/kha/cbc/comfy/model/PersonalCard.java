package com.kha.cbc.comfy.model;

import com.kha.cbc.comfy.model.common.BaseCardModel;

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
