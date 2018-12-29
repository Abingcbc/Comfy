package com.kha.cbc.comfy.view.guide;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.view.main.MainActivity;

public class GuideActivity extends AppIntro {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setZoomAnimation();

        SliderPage page1 = new SliderPage();
        page1.setTitle("规划个人日程");
        page1.setImageDrawable(R.drawable.personal_guide);
        page1.setBgColor(R.color.avoscloud_blue);
        addSlide(AppIntroFragment.newInstance(page1));

        SliderPage page2 = new SliderPage();
        page2.setTitle("团队协作 高效工作");
        page2.setImageDrawable(R.drawable.team_guide);
        page2.setBgColor(R.color.cyan);
        addSlide(AppIntroFragment.newInstance(page2));

        SliderPage page3 = new SliderPage();
        page3.setTitle("掌握app使用情况 提高效率");
        page3.setImageDrawable(R.drawable.efficiency_guide);
        page3.setBgColor(R.color.yello);
        addSlide(AppIntroFragment.newInstance(page3));

        showSkipButton(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        SharedPreferences preferences = getSharedPreferences("first", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstStart", false);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        SharedPreferences preferences = getSharedPreferences("first", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstStart", false);
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
