package com.github.fozruk.streamcheckerguitest.vlcgui.chat;

/**
 * Created by Philipp on 12.08.2015.
 *
 */
public interface IChat {
    void _sendMessage(String channelname, String message);
    boolean _isConnected();
    String getChannelName();
    String getUsername();
    void disconnect();
}
