package com.kha.cbc.comfy.data.network.dto;

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
