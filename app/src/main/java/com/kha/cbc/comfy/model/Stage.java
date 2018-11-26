package com.kha.cbc.comfy.model;

import java.util.ArrayList;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public class Stage {

    public ArrayList<TeamCard> teamCardList;
    public String title;

    public Stage(ArrayList<TeamCard> teamCardList, String title) {
        this.teamCardList = teamCardList;
        this.title = title;
    }
}
