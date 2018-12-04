package com.kha.cbc.comfy.view.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.entity.GDUser;
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.greendao.gen.GDUserDao;
import com.kha.cbc.comfy.model.TabEntity;
import com.kha.cbc.comfy.model.User;
import com.kha.cbc.comfy.presenter.MainPresenter;
import com.kha.cbc.comfy.presenter.Presenter;
import com.kha.cbc.comfy.view.common.ActivityManager;
import com.kha.cbc.comfy.view.common.BaseActivityWithPresenter;
import com.kha.cbc.comfy.view.efficient.EfficientFragment;
import com.kha.cbc.comfy.view.login.LoginActivity;
import com.kha.cbc.comfy.view.personal.PersonalFragment;
import com.kha.cbc.comfy.view.settings.SettingsActivity;
import com.kha.cbc.comfy.view.team.TeamFragment;
import org.jetbrains.annotations.NotNull;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivityWithPresenter
        implements MainView,
        NavigationView.OnNavigationItemSelectedListener {

    CommonTabLayout commonTabLayout;
    ArrayList<CustomTabEntity> tabEntityList;
    int currentPosition;

    ArrayList<Fragment> fragmentList;
    PersonalFragment personalFragment;
    TeamFragment teamFragment;
    EfficientFragment efficientFragment;


    GDPersonalTaskDao taskDao;
    FloatingActionButton fab;
    Boolean logged = false;
    String sessionToken;

    MainPresenter presenter;

    @Override
    protected void onResume() {
        super.onResume();
        initNavigationView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intentFrom = getIntent();

        GDUserDao userDao = ((ComfyApp) getApplication()).getDaoSession().getGDUserDao();
        List<GDUser> userList = userDao.loadAll();
        if (userList.size() != 1 || userList.get(0) == null ||
                userList.get(0).getUsername() == null || userList.get(0).getSessionToken() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            User.INSTANCE.fromGDUser(userList.get(0));
        }

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.main_plus_fab);
        fab.setOnClickListener(view -> {
            if (currentPosition == 0)
                personalFragment.plusTask();
            else
                teamFragment.plusTask();
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initNavigationView();

        init();

        ActivityManager.INSTANCE.plusAssign(this);
    }


    void initNavigationView(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ImageView avatarImageView = (ImageView) headerView.findViewById(R.id.drawer_avatar_imageView);
        TextView usernameTextView = headerView.findViewById(R.id.drawer_username_textView);
        TextView sessionTokenTextView = headerView.findViewById(R.id.drawer_sessionToken_textView);
        usernameTextView.setText(User.INSTANCE.getUsername());
        sessionTokenTextView.setText(User.INSTANCE.getSessionToken());
    }



    void init() {

        //TODO: Add Charted App Usage View
        taskDao = ((ComfyApp) getApplication())
                .getDaoSession().getGDPersonalTaskDao();
        presenter = new MainPresenter(this);
        commonTabLayout = findViewById(R.id.tabLayout);

        fragmentList = new ArrayList<>();
        personalFragment = PersonalFragment.getInstance(taskDao);
        fragmentList.add(personalFragment);
        teamFragment = TeamFragment.getInstance();
        fragmentList.add(teamFragment);
        efficientFragment = new EfficientFragment();
        fragmentList.add(efficientFragment);


        tabEntityList = new ArrayList<>();
        tabEntityList.add(new TabEntity("个人", R.drawable.account));
        tabEntityList.add(new TabEntity("团队", R.drawable.account_supervisor));
        tabEntityList.add(new TabEntity("效率", R.drawable.finance));
        commonTabLayout.setTabData(tabEntityList, this, R.id.frag_change, fragmentList);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onTabSelect(int position) {
                currentPosition = position;
                if (position == 2) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

//    @Override
//    protected void onStop() {
//        GDUserDao userDao = ((ComfyApp) getApplication()).getDaoSession().getGDUserDao();
//        List<GDUser> userList = userDao.loadAll();
//        if(userList != null && userList.size() > 0){
//            for(GDUser item : userList){
//                userDao.delete(item);
//            }
//        }
//        GDUser newUser = new GDUser(User.INSTANCE.getUsername(), User.INSTANCE.getSessionToken());
//        userDao.insert(newUser);
//        super.onStop();
//    }

    //--------------------------------------------

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            final Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroyed();
    }

    @NotNull
    @Override
    public Presenter getPresenter() {
        return presenter;
    }
}
