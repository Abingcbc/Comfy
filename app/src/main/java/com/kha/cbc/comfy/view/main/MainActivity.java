package com.kha.cbc.comfy.view.main;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.data.entity.GDPersonalTask;
import com.kha.cbc.comfy.greendao.gen.DaoSession;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.view.personal.PersonalTaskAdapter;
import com.kha.cbc.comfy.data.model.PersonalTask;
import com.kha.cbc.comfy.view.plus.PersonalPlusTaskActivity;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements CardStackView.ItemExpendListener, MainView,
        NavigationView.OnNavigationItemSelectedListener{

    CardStackView cardStackView;
    List<PersonalTask> taskList;
    PersonalTaskAdapter personalTaskAdapter;
    List<Integer> backColor;
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_plus_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonalPlusTaskActivity.class);
                startActivityForResult(intent, 1);
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
        presenter = new MainPresenter(this);
        personalTaskAdapter = new PersonalTaskAdapter(this);
        backColor = new LinkedList<>();
        cardStackView = findViewById(R.id.cardStackView);
        cardStackView.setAdapter(personalTaskAdapter);
        cardStackView.setItemExpendListener(this);
        //personalTaskAdapter.updateData(backColor, taskList);
        //动画效果
        cardStackView.setAnimatorAdapter(new AllMoveDownAnimatorAdapter(cardStackView));
        cardStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(cardStackView));
        cardStackView.setAnimatorAdapter(new UpDownStackAnimatorAdapter(cardStackView));
        DaoSession daoSession = ((ComfyApp) getApplication()).getDaoSession();
        GDPersonalTaskDao taskDao = daoSession.getGDPersonalTaskDao();
        presenter.loadAllTasksFromDB(taskDao);
    }

    @Override
    public void onItemExpend(boolean expend) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            //1为添加task
            case 1:
                if (resultCode == RESULT_OK) {
                    DaoSession daoSession = ((ComfyApp) getApplication()).getDaoSession();
                    GDPersonalTaskDao taskDao = daoSession.getGDPersonalTaskDao();
                    presenter.loadAllTasksFromDB(taskDao);
                }
            case 2:
                if (resultCode == RESULT_OK) {

                }
        }
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

    @Override
    public void OnLoadAllFromDBSuccess(List<PersonalTask> personalTaskList) {
        GDPersonalTaskDao taskDao = ((ComfyApp) getApplication())
                .getDaoSession().getGDPersonalTaskDao();
        List<GDPersonalTask> personalTasks = taskDao.queryBuilder().list();
        taskList = new LinkedList<>();
        if (personalTasks.isEmpty())
            return;
        else {
            for (GDPersonalTask task : personalTasks) {
                taskList.add(new PersonalTask(task));
            }
            //TODO:这里的backColor是固定的，到后期确定涂色表要进行添加
            backColor = new LinkedList<>();
            for (int i = 0; i < taskList.size(); i++) {
                backColor.add(R.color.avoscloud_blue);
            }
            personalTaskAdapter.updateData(backColor, taskList);
        }
    }


}
