package com.kha.cbc.comfy.view.plus;

import com.kha.cbc.comfy.model.TeamTask;

public interface PlusTaskView {
    void onPostSuccess(TeamTask teamTask);

    void onPostError(Throwable e);
}
