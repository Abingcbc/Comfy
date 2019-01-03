package com.kha.cbc.comfy.model;

import java.util.ArrayList;

public class Stage {

    public ArrayList<TeamCard> teamCardList;
    public String title;
    public String objectId;
    public int index;

    public Stage(ArrayList<TeamCard> teamCardList, String title, String objectId, int index) {
        this.teamCardList = teamCardList;
        this.title = title;
        this.objectId = objectId;
        this.index = index;
    }
}
