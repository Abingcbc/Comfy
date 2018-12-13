package com.kha.cbc.comfy.view.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.entity.GDUser;
import com.kha.cbc.comfy.greendao.gen.GDAvatarDao;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.greendao.gen.GDUserDao;
import com.kha.cbc.comfy.model.TabEntity;
import com.kha.cbc.comfy.model.User;
import com.kha.cbc.comfy.presenter.AvatarPresenter;
import com.kha.cbc.comfy.presenter.MainPresenter;
import com.kha.cbc.comfy.presenter.Presenter;
import com.kha.cbc.comfy.view.common.ActivityManager;
import com.kha.cbc.comfy.view.common.AvatarView;
import com.kha.cbc.comfy.view.common.BaseActivityWithPresenter;
import com.kha.cbc.comfy.view.efficient.EfficientFragment;
import com.kha.cbc.comfy.view.login.LoginActivity;
import com.kha.cbc.comfy.view.personal.PersonalFragment;
import com.kha.cbc.comfy.view.settings.SettingsActivity;
import com.kha.cbc.comfy.view.team.TeamFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivityWithPresenter
        implements MainView,
        NavigationView.OnNavigationItemSelectedListener, AvatarView {

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
    AvatarPresenter avatartPresenter;

    @Override
    protected void onResume() {
        super.onResume();
        initNavigationView();
    }

    GDAvatarDao avatarDao;

    @NotNull
    @Override
    public GDAvatarDao getAvatarDao() {
        return avatarDao;
    }

    @Override
    public void setAvatarDao(@NotNull GDAvatarDao gdAvatarDao) {
        avatarDao = gdAvatarDao;
    }

    @Override
    public void uploadAvatarFinish(@NotNull String url) {

    }

    @Override
    public void downloadAvatarFinish(@NotNull List<Pair<String, String>> urlPairs) {

    }

    @Override
    public void uploadProgressUpdate(@Nullable Integer progress) {

    }

    @Override
    public void setProgressBarVisible() {

    }

    @Override
    public void downloadAvatarFinish(@NotNull String url) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        CircleImageView avatarImageView = headerView.findViewById(R.id.drawer_avatar_imageView);
        Glide.with(this).load(url).into(avatarImageView);
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

        setAvatarDao(((ComfyApp)getApplication()).getDaoSession().getGDAvatarDao());
        avatartPresenter = new AvatarPresenter(this);
        presenter = new MainPresenter(this);

        GDUserDao userDao = ((ComfyApp) getApplication()).getDaoSession().getGDUserDao();
        List<GDUser> userList = userDao.loadAll();
        if (userList.size() != 1 || userList.get(0) == null ||
                userList.get(0).getUsername() == null || userList.get(0).getSessionToken() == null
                || userList.get(0).getObjectId() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            User.INSTANCE.fromGDUser(userList.get(0));
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
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                @Override
                public void onDrawerOpened(View drawerView) {
                    avatartPresenter.loadAvatar();
                }
            };
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            initNavigationView();

            init();

            ActivityManager.INSTANCE.plusAssign(this);
        }

//        AMAP Test

//        final long serviceId = Long.parseLong(BuildConfig.COMFYGROUPTRACKSERVICEID);  // 这里填入前面创建的服务id
//        final String terminalName = "testglobal";   // 唯一标识某个用户或某台设备的名称，可根据您的业务自行选择
//        final AMapTrackClient aMapTrackClient = new AMapTrackClient(getApplicationContext());
//        final OnTrackLifecycleListener onTrackLifecycleListener = new OnTrackLifecycleListener() {
//
//            @Override
//            public void onBindServiceCallback(int i, String s) {
//
//            }
//
//            @Override
//            public void onStopGatherCallback(int i, String s) {
//
//            }
//
//            @Override
//            public void onStopTrackCallback(int i, String s) {
//
//            }
//
//            @Override
//            public void onStartGatherCallback(int status, String msg) {
//                if (status == ErrorCode.TrackListen.START_GATHER_SUCEE ||
//                        status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
//                    Toast.makeText(MainActivity.this, "定位采集开启成功！", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "定位采集启动异常，" + msg, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onStartTrackCallback(int status, String msg) {
//                if (status == ErrorCode.TrackListen.START_TRACK_SUCEE ||
//                        status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK ||
//                        status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
//                    // 服务启动成功，继续开启收集上报
//                    aMapTrackClient.startGather(this);
//                } else {
//                    Toast.makeText(MainActivity.this, "轨迹上报服务服务启动异常，" + msg, Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//        aMapTrackClient.queryTerminal(new QueryTerminalRequest(serviceId, terminalName), new OnTrackListener() {
//
//            @Override
//            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
//
//            }
//
//            @Override
//            public void onDistanceCallback(DistanceResponse distanceResponse) {
//
//            }
//
//            @Override
//            public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
//
//            }
//
//            @Override
//            public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
//
//            }
//
//            @Override
//            public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
//
//            }
//
//            @Override
//            public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
//
//            }
//
//            @Override
//            public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {
//
//            }
//
//            @Override
//            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
//                if (queryTerminalResponse.isSuccess()) {
//                    if (queryTerminalResponse.getTid() <= 0) {
//                        // terminal还不存在，先创建
//                        aMapTrackClient.addTerminal(new AddTerminalRequest(terminalName, serviceId), new OnTrackListener() {
//
//                            @Override
//                            public void onQueryTerminalCallback(QueryTerminalResponse queryTerminalResponse) {
//
//                            }
//
//                            @Override
//                            public void onDistanceCallback(DistanceResponse distanceResponse) {
//
//                            }
//
//                            @Override
//                            public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
//
//                            }
//
//                            @Override
//                            public void onHistoryTrackCallback(HistoryTrackResponse historyTrackResponse) {
//
//                            }
//
//                            @Override
//                            public void onQueryTrackCallback(QueryTrackResponse queryTrackResponse) {
//
//                            }
//
//                            @Override
//                            public void onAddTrackCallback(AddTrackResponse addTrackResponse) {
//
//                            }
//
//                            @Override
//                            public void onParamErrorCallback(ParamErrorResponse paramErrorResponse) {
//
//                            }
//
//                            @Override
//                            public void onCreateTerminalCallback(AddTerminalResponse addTerminalResponse) {
//                                if (addTerminalResponse.isSuccess()) {
//                                    // 创建完成，开启猎鹰服务
//                                    long terminalId = addTerminalResponse.getTid();
//                                    aMapTrackClient.startTrack(new TrackParam(serviceId, terminalId), onTrackLifecycleListener);
//                                } else {
//                                    // 请求失败
//                                    Toast.makeText(MainActivity.this, "请求失败，" + addTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    } else {
//                        // terminal已经存在，直接开启猎鹰服务
//                        long terminalId = queryTerminalResponse.getTid();
//                        aMapTrackClient.startTrack(new TrackParam(serviceId, terminalId), onTrackLifecycleListener);
//                    }
//                } else {
//                    // 请求失败
//                    Toast.makeText(MainActivity.this, "请求失败，" + queryTerminalResponse.getErrorMsg(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }


    void initNavigationView(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.drawer_username_textView);
        TextView sessionTokenTextView = headerView.findViewById(R.id.drawer_sessionToken_textView);
        usernameTextView.setText(User.INSTANCE.getUsername());
        sessionTokenTextView.setText(User.INSTANCE.getSessionToken());
    }



    void init() {

        //TODO: Add Charted App Usage View
        taskDao = ((ComfyApp) getApplication())
                .getDaoSession().getGDPersonalTaskDao();
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
