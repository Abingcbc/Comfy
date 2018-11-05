package com.kha.cbc.comfy.cards.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.kha.cbc.comfy.MainActivity;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.cards.personal.presenter.PersonalPlusCardPresenter;

/**
 * Created by ABINGCBC
 * on 2018/11/4
 */

public class PersonalPlusCardActivity extends AppCompatActivity {

    PersonalPlusCardPresenter presenter = new PersonalPlusCardPresenter();

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
                Intent intent = new Intent(PersonalPlusCardActivity.this, MainActivity.class);
                intent.putExtra("isSuccess", "failed");
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });
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
                presenter.storeCard((TextView)findViewById(R.id.input_card_title),
                        (TextView)findViewById(R.id.input_card_description));
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
