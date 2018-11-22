package com.kha.cbc.comfy.view.main;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.flyco.tablayout.*;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.model.TabEntity;
import com.kha.cbc.comfy.presenter.MainPresenter;
import com.kha.cbc.comfy.view.login.LoginActivity;
import com.kha.cbc.comfy.view.personal.PersonalFragment;
import com.kha.cbc.comfy.view.team.TeamFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainView,
        NavigationView.OnNavigationItemSelectedListener{

    MainPresenter presenter;
    ViewPager viewPager;
    CommonTabLayout commonTabLayout;
    ArrayList<Fragment> fragmentList;
    ArrayList<CustomTabEntity> tabEntityList;
    PersonalFragment personalFragment;
    MainFragmentAdapter adapter;
    GDPersonalTaskDao taskDao;
    Boolean logged = false;
    String user;
    String sessionToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intentFrom = getIntent();
        user = intentFrom.getStringExtra("username");
        sessionToken = intentFrom.getStringExtra("sessionToken");
        if(user == null || sessionToken == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
        else{
            //TODO: need to be deleted, just for test
            Toast.makeText(this, sessionToken, Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_plus_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personalFragment.plusTask();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //LeanCloud Test Code
//        AVObject testObject = new AVObject("TestObject");
//        testObject.put("words","Hello World!");
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if(e == null){
//                    Log.d("saved","success!");
//                }
//            }
//        });
        init();
    }

    void init() {
        taskDao = ((ComfyApp) getApplication())
                .getDaoSession().getGDPersonalTaskDao();
        presenter = new MainPresenter(this);
        //viewPager = findViewById(R.id.viewPager);
        commonTabLayout = findViewById(R.id.tabLayout);
        personalFragment = PersonalFragment.getInstance(taskDao);
        fragmentList = new ArrayList<>();
        fragmentList.add(personalFragment);
        fragmentList.add(TeamFragment.getInstance());
        fragmentList.add(PersonalFragment.getInstance(taskDao));
        adapter = new MainFragmentAdapter(getSupportFragmentManager(), fragmentList);
        //viewPager.setAdapter(adapter);
        tabEntityList = new ArrayList<>();
        tabEntityList.add(new TabEntity("个人", R.drawable.account));
        tabEntityList.add(new TabEntity("团队", R.drawable.account_supervisor));
        tabEntityList.add(new TabEntity("效率", R.drawable.finance));
        commonTabLayout.setTabData(tabEntityList, this, R.id.frag_change, fragmentList);
    }

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

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void saveCardToLocal() {

    }

    @Override
    public void saveTaskToLocal() {

    }

    @Override
    public void saveCardToCloud() {

    }

    @Override
    public void saveTaskToCloud() {
    }

}
