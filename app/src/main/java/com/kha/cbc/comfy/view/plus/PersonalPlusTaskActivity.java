package com.kha.cbc.comfy.view.plus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.data.entity.GDPersonalTask;
import com.kha.cbc.comfy.data.model.PersonalTask;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;

public class PersonalPlusTaskActivity extends AppCompatActivity {

    PersonalPlusTaskPresenter presenter = new PersonalPlusTaskPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_plus_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.personal_plus_toolbar);
        setSupportActionBar(toolbar);

        EditText editText = findViewById(R.id.input_task_title);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE ||
                        (keyEvent!=null &&
                                keyEvent.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    Intent intent  = new Intent();
                    String taskTitle = textView.getText().toString();
                    setResult(RESULT_OK, intent);
                    GDPersonalTaskDao taskDao = ((ComfyApp) getApplication())
                            .getDaoSession().getGDPersonalTaskDao();
                    GDPersonalTask personalTask = new GDPersonalTask(new PersonalTask(taskTitle));
                    taskDao.insert(personalTask);
                    //推送到服务器
                    //presenter.addTask(textView.getText().toString());
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

}
