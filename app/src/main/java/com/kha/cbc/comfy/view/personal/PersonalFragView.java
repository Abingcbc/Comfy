package com.kha.cbc.comfy.view.personal;

import com.kha.cbc.comfy.model.PersonalTask;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/17
 */
public interface PersonalFragView {

    void onLoadAllFromDBSuccess(List<PersonalTask> taskList);
    void onLoadAllFromDBError(Throwable e);

}
