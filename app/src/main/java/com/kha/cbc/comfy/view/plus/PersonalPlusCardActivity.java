package com.kha.cbc.comfy.view.plus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.data.entity.GDPersonalCard;
import com.kha.cbc.comfy.data.entity.GDPersonalTask;
import com.kha.cbc.comfy.data.model.PersonalCard;
import com.kha.cbc.comfy.data.model.PersonalTask;
import com.kha.cbc.comfy.greendao.gen.GDPersonalCardDao;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.view.main.MainActivity;
import com.kha.cbc.comfy.R;

/**
 * Created by ABINGCBC
 * on 2018/11/4
 */

public class PersonalPlusCardActivity extends AppCompatActivity implements PersonalPlusView{

    PersonalPlusCardPresenter presenter = new PersonalPlusCardPresenter();
    String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_plus_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.personal_plus_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
        Intent intent = getIntent();
        taskId = intent.getStringExtra("taskId");
    }

    //绑定完成按钮到toolbar中
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.plus_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.plus_success:
                //TODO: 此时只在本地添加一条card，暂时觉得没有必要开多线程，但以后服务端同步要开
                GDPersonalCardDao cardDao = ((ComfyApp) getApplication())
                        .getDaoSession().getGDPersonalCardDao();
                        TextView titleView = findViewById(R.id.input_card_title);
                TextView descriptionView = findViewById(R.id.input_card_description);
                String title = titleView.getText().toString();
                String description = descriptionView.getText().toString();
                cardDao.insert(new GDPersonalCard(title, description, taskId));

                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
