package com.kha.cbc.comfy.cards.personal.presenter;

import android.content.Context;
import android.content.Intent;
import com.kha.cbc.comfy.cards.personal.PlusCardActivity;

/**
 * Created by CBC
 * on 2018/11/4
 */

public class PersonalPlusCardPresenter {

    public void OnClicked(Context context, String taskId) {
        Intent intent = new Intent(context, PlusCardActivity.class);
        intent.putExtra("taskId", taskId);
        context.startActivity(intent);
    }
}
