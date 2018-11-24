package com.kha.cbc.comfy.model;

import com.kha.cbc.comfy.model.common.BaseCardModel;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public class TeamCard extends BaseCardModel {

    String executor;

    public TeamCard(String taskId, String executor, String title) {
        super(taskId);
        this.title = title;
        this.executor = executor;
    }
}
