package com.kha.cbc.comfy.view.personal;

import com.kha.cbc.comfy.data.model.PersonalTask;
import com.kha.cbc.comfy.greendao.gen.GDPersonalTaskDao;

import java.util.List;

/**
 * Created by ABINGCBC
 * on 2018/11/17
 */
public interface PersonalFragView {

    void OnLoadAllFromDBSuccess(List<PersonalTask> taskList);

}
