package com.kha.cbc.comfy.model;

import com.kha.cbc.comfy.data.network.dto.TeamTaskDto;
import com.kha.cbc.comfy.model.common.BaseTaskModel;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/19
 */
public class TeamTask extends BaseTaskModel {

    String founder;
    String objectId;
    List<String> memberImageUrls;

    public TeamTask(String title, String founder, String objectId) {
        this.title = title;
        this.founder = founder;
        this.objectId = objectId;
    }

    public TeamTask(TeamTaskDto dto) {
        this.founder = dto.creatorName;
        this.title = dto.title;
        this.objectId = dto.objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public List<String> getMemberImageUrls() {
        return memberImageUrls;
    }

    public void setMemberImageUrls(List<String> memberImageUrls) {
        this.memberImageUrls = memberImageUrls;
    }
}
