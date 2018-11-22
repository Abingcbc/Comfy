package com.kha.cbc.comfy.data.model;

import com.kha.cbc.comfy.common.BaseTaskModel;

/**
 * Created by ABINGCBC
 * on 2018/11/19
 */
public class TeamTask extends BaseTaskModel {

    String founder;
    String imageUrl;

    public TeamTask(String title, String founder, String imageUrl) {
        this.title = title;
        this.founder = founder;
        this.imageUrl = imageUrl;
    }

    String getImageUrl() {
        return imageUrl;
    }
}
