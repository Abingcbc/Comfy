package com.kha.cbc.comfy.cards.common;

/**
 * Created by CBC
 * on 2018/11/2
 */

public class BaseCardModel {

    protected String title;
    protected String description;
    protected String taskId;

    protected BaseCardModel(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskId() {
        return taskId;
    }
}
