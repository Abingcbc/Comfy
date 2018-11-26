package com.kha.cbc.comfy.view.main;

import com.kha.cbc.comfy.view.common.BaseView;

/**
 * Created by ABINGCBC
 * on 2018/11/5
 */

public interface MainView extends BaseView {

    void saveCardToLocal();

    void saveTaskToLocal();

    void saveCardToCloud();

    void saveTaskToCloud();

}
