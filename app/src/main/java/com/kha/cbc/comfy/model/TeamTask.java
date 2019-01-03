package com.kha.cbc.comfy.model;

import com.kha.cbc.comfy.model.common.BaseTaskModel;
import java.util.List;

public class TeamTask extends BaseTaskModel {

    String founder;
    String objectId;
    List<String> memberImageUrls;

    public TeamTask(String title, String founder, String objectId) {
        this.title = title;
        this.founder = founder;
        this.objectId = objectId;
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
