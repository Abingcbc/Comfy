package com.kha.cbc.comfy.view.plus;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.kha.cbc.comfy.ComfyApp;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.entity.GDPersonalTask;
import com.kha.cbc.comfy.model.PersonalTask;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;
import com.kha.cbc.comfy.presenter.PersonalPlusTaskPresenter;
import com.kha.cbc.comfy.view.common.ActivityManager;

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

        ActivityManager.INSTANCE.plusAssign(this);
    }

    @Override
    protected void onDestroy() {
        ActivityManager.INSTANCE.minusAssign(this);
        super.onDestroy();
    }
}
