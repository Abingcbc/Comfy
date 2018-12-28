package com.kha.cbc.comfy.view.common;

/**
 * Created by ABINGCBC
 * on 2018/11/24
 */
public interface BaseRefreshView {

    void refresh(boolean b);

    void onComplete();
    void onChatReady(String conversationId);
}
