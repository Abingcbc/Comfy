package com.kha.cbc.comfy.view.team;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import com.kha.cbc.comfy.model.PersonalTask;
import com.tmall.ultraviewpager.UltraViewPagerAdapter;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public class TeamDetailFragAdapter extends FragmentPagerAdapter {

    List<StageFragment> fragmentList;

    TeamDetailFragAdapter(FragmentManager manager, List<StageFragment> fragmentList) {
        super(manager);
        this.fragmentList = fragmentList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return FragmentPagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
