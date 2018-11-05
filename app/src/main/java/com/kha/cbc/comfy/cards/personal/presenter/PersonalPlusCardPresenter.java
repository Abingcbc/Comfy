package com.kha.cbc.comfy.cards.personal.presenter;

import android.widget.TextView;
import com.kha.cbc.comfy.cards.common.BasePresenter;

/**
 * Created by ABINGCBC
 * on 2018/11/4
 */

public class PersonalPlusCardPresenter extends BasePresenter {

    public void storeCard(TextView cardName, TextView cardDescription) {
        String title = cardName.getText().toString();
        String description = cardDescription.getText().toString();

    }
}
