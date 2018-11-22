package com.kha.cbc.comfy.common;

import android.util.Log;
import com.kha.cbc.comfy.common.BaseCardModel;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/2
 */

public class BaseTaskModel {

    protected String title;
    protected String id;
    protected List<BaseCardModel> cardModelList;

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

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}