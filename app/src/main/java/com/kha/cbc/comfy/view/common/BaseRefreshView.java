package com.kha.cbc.comfy.view.common;

public interface BaseRefreshView {

    void refresh(boolean b);

    void onComplete();
    void onChatReady(String conversationId);
}
