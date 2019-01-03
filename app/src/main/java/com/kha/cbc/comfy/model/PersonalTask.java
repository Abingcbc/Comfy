package com.kha.cbc.comfy.model;

import com.kha.cbc.comfy.entity.GDPersonalCard;
import com.kha.cbc.comfy.entity.GDPersonalTask;
import com.kha.cbc.comfy.model.common.BaseCardModel;
import com.kha.cbc.comfy.model.common.BaseTaskModel;
import org.greenrobot.greendao.annotation.Entity;

import java.util.LinkedList;
import java.util.List;

@Entity
public class PersonalTask extends BaseTaskModel {

    public PersonalTask(String title) {
        this.title = title;
        this.id = initId(title);
        cardModelList = new LinkedList<>();
        cardModelList.add(new PlusCard(id));
    }

    public PersonalTask(GDPersonalTask task) {
        this.title = task.getTitle();
        this.id = task.getId();
        this.cardModelList = new LinkedList<>();
        for (GDPersonalCard card : task.getPersonalCardList()) {
            this.cardModelList.add(new PersonalCard(card));
        }
        cardModelList.add(new PlusCard(id));
    }

    public List<BaseCardModel> getCards() {
        return cardModelList;
    }

    public String getTitle() {
        return title;
    }
}
