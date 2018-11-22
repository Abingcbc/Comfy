package com.kha.cbc.comfy.view.main;

import com.kha.cbc.comfy.data.entity.GDPersonalTask;
import com.kha.cbc.comfy.data.model.PersonalTask;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/5
 */

public interface MainView {

    void saveCardToLocal();
    void saveTaskToLocal();
    void saveCardToCloud();
    void saveTaskToCloud();

}
