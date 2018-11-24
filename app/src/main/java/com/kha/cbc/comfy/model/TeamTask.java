package com.kha.cbc.comfy.model;

import com.kha.cbc.comfy.data.network.dto.TeamTaskDto;
import com.kha.cbc.comfy.model.common.BaseTaskModel;

/**
 * Created by ABINGCBC
 * on 2018/11/19
 */
public class TeamTask extends BaseTaskModel {

    String founder;
    String imageUrl;
    String objectId;

    public TeamTask(String title, String founder, String imageUrl, String objectId) {
        this.title = title;
        this.founder = founder;
        this.imageUrl = imageUrl;
        this.objectId = objectId;
    }

    public TeamTask(TeamTaskDto dto) {
        this.founder = dto.creatorName;
        this.title = dto.title;
        this.objectId = dto.objectId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getObjectId() {
        return objectId;
    }
}
