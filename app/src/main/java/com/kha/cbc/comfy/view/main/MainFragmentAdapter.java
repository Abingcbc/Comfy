package com.kha.cbc.comfy.view.main;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/17
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList;
    List<String> titleList;

    MainFragmentAdapter(FragmentManager manager,
                        List<Fragment> fragments) {
        super(manager);
        this.fragmentList = fragments;
        this.titleList = new ArrayList<>();
        //TODO:可在此修改语言
        this.titleList.add("个人");
        this.titleList.add("团队");
        this.titleList.add("效率");
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
