package com.kha.cbc.comfy.data.network.dto;

/**
 * Created by ABINGCBC
 * on 2018/11/23
 */
public class TeamTaskDto {

    public String creatorName;
    public String title;
    public String objectId;

    TeamTaskDto(String title, String creatorName, String objectId) {
        this.title = title;
        this.creatorName = creatorName;
        this.objectId = objectId;
    }
}
