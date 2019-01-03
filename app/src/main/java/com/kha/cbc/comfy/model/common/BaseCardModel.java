package com.kha.cbc.comfy.model.common;

import android.util.Log;

import java.util.Calendar;

public class BaseCardModel {

    protected String id;
    protected String title;
    protected String description;
    protected String taskId;
    protected String remindDate;
    protected boolean isRemind;

    protected BaseCardModel(String taskId) {
        this.taskId = taskId;
    }

    public String getId() {
        return id;
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

    public String getRemindDate() {
        return remindDate;
    }

    public boolean isRemind() {
        return isRemind;
    }

    public String initId(String name) {
        StringBuilder builder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        builder.append(calendar.get(Calendar.MONTH) + "-");
        builder.append(calendar.get(Calendar.DAY_OF_MONTH) + "-");
        builder.append(calendar.get(Calendar.HOUR_OF_DAY) + "-");
        builder.append(calendar.get(Calendar.MINUTE) + "-");
        builder.append(calendar.get(Calendar.SECOND) + "-");
        builder.append(name);
        Log.d("id:-----------", builder.toString());
        return builder.toString();
    }
}
