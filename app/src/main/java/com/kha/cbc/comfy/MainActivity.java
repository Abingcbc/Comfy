package com.kha.cbc.comfy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.kha.cbc.comfy.cards.personal.view.PersonalTaskAdapter;
import com.kha.cbc.comfy.cards.personal.model.PersonalCard;
import com.kha.cbc.comfy.cards.personal.model.PersonalTask;
import com.loopeer.cardstack.AllMoveDownAnimatorAdapter;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.UpDownAnimatorAdapter;
import com.loopeer.cardstack.UpDownStackAnimatorAdapter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements CardStackView.ItemExpendListener{

    CardStackView cardStackView;

    Integer[] backColor = {
            R.color.avoscloud_blue,
            R.color.avoscloud_timestamp_gray,
            R.color.colorPrimary,
            R.color.colorAccent,
            //R.color.colorPrimaryDark,
            //R.color.avoscloud_feedback_thread_actionbar_blue
    };

    String[] name = {"数据结构","计算机网络","编译原理","C语言"/*,"算法设计","FPGA编程"*/};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


        cardStackView = findViewById(R.id.cardStackView);
        PersonalTaskAdapter personalTaskAdapter = new PersonalTaskAdapter(this);
        cardStackView.setAdapter(personalTaskAdapter);
        cardStackView.setItemExpendListener(this);
        List<PersonalTask> taskList = new LinkedList<>();
        for (String str : name) {
            PersonalTask personalTask = new PersonalTask(str);
            personalTask.addCard(new PersonalCard("aaaa", str, str));
            taskList.add(personalTask);
        }
        personalTaskAdapter.updateData(Arrays.asList(backColor), taskList);
        //动画效果
        cardStackView.setAnimatorAdapter(new AllMoveDownAnimatorAdapter(cardStackView));
        cardStackView.setAnimatorAdapter(new UpDownAnimatorAdapter(cardStackView));
        cardStackView.setAnimatorAdapter(new UpDownStackAnimatorAdapter(cardStackView));
    }

    @Override
    public void onItemExpend(boolean expend) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

    }
}
