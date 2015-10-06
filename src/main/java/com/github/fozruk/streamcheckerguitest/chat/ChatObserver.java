package com.github.fozruk.streamcheckerguitest.chat;

import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;

/**
 * Created by Philipp on 12.08.2015.
 */
public interface ChatObserver {
    void _onMessage(ChatMessage message);
    void _onDisconnect();
}
