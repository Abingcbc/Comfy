package com.kha.cbc.comfy.cards.personal.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.kha.cbc.comfy.cards.common.BasePresenter;
import com.kha.cbc.comfy.cards.personal.PersonalPlusCardActivity;

/**
 * Created by ABINGCBC
 * on 2018/11/4
 */

public class PersonalPresenter extends BasePresenter {

    public void OnPlusCardClicked(Context context, String taskId) {
        Intent intent = new Intent(context, PersonalPlusCardActivity.class);
        intent.putExtra("taskId", taskId);
        ((Activity)context).startActivityForResult(intent, 1);
    }

}
