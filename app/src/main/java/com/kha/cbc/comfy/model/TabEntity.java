package com.kha.cbc.comfy.model;


import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by ABINGCBC
 * on 2018/11/22
 */
public class TabEntity implements CustomTabEntity {

    String title;
    int icon;

    public TabEntity(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return icon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return icon;
    }
}
