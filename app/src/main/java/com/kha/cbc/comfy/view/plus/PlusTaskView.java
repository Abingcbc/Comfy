package com.kha.cbc.comfy.view.plus;

import com.kha.cbc.comfy.model.TeamTask;

/**
 * Created by ABINGCBC
 * on 2018/11/23
 */
public interface PlusTaskView {
    void onPostSuccess(TeamTask teamTask);

    void onPostError(Throwable e);
}
