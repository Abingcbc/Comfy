package com.kha.cbc.comfy.cards.personal.model;

import com.kha.cbc.comfy.cards.common.BaseCardModel;
import com.kha.cbc.comfy.cards.common.BaseTaskModel;
import com.kha.cbc.comfy.cards.common.PlusCard;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

/**
 * Created by ABINGCBC
 * on 2018/11/2
 */

public class PersonalTask extends BaseTaskModel {

    public PersonalTask(String title) {
        this.title = title;
        this.id = initId(title);
        cardModelList = new LinkedList<>();
        cardModelList.add(new PlusCard(id));
    }

    public List<BaseCardModel> getCards() {
        return cardModelList;
    }

    public String getTitle() {
        return title;
    }
}
