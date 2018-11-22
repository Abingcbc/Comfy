package com.kha.cbc.comfy.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.kha.cbc.comfy.R;
import com.kha.cbc.comfy.view.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

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

        //Leancloud Test Code
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
    }
}

