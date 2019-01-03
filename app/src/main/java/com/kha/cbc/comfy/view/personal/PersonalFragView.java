package com.kha.cbc.comfy.view.personal;

import com.kha.cbc.comfy.model.PersonalTask;

import java.util.List;

public interface PersonalFragView {

    void onLoadAllFromDBSuccess(List<PersonalTask> taskList);

    void onLoadAllFromDBError(Throwable e);

}
