package com.github.fozruk.streamcheckerguitest.chat.test;

import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.chat.ChatObserver;
import com.github.fozruk.streamcheckerguitest.chat.IChat;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Philipp on 07.10.2015.
 */
public class MockChat implements IChat {

    private ChatObserver observer;

    @Override
    public void _sendMessage(String channelname, String message) {
        this.observer._onMessage(new ChatMessage("test test test " +
                "23234234234234"));
    }

    @Override
    public boolean _isConnected() {
        return false;
    }

    @Override
    public String getChannelName() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public String[] getUserList() throws MalformedURLException, ReadingWebsiteFailedException, JSONException {
        return new String[0];
    }

    @Override
    public void start() throws IOException, ReadingWebsiteFailedException, JSONException {

    }

    @Override
    public void setObserver(ChatObserver observer) {

    }
}
