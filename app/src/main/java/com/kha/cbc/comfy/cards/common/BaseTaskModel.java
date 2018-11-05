package com.kha.cbc.comfy.cards.common;

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
        builder.append(calendar.get(Calendar.YEAR) + "-");
        builder.append(calendar.get(Calendar.MONTH) + "-");
        builder.append(calendar.get(Calendar.DAY_OF_MONTH) + "-");
        builder.append(calendar.get(Calendar.HOUR_OF_DAY) + "-");
        builder.append(calendar.get(Calendar.MINUTE) + "-");
        builder.append(title);
        return builder.toString();
    }

    public void addCard(BaseCardModel cardModel) {
        cardModelList.add(cardModelList.size() - 1, cardModel);
    }
}
