package com.github.fozruk.streamcheckerguitest.tests;

import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.ChatObserver;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.IChat;
import com.github.fozruk.streamcheckerguitest.vlcgui.controller.VlcLivestreamController;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Philipp on 04.10.2015.
 */
public class TestChatImpl implements IChat {
    VlcLivestreamController ctrl ;
    public TestChatImpl(VlcLivestreamController vlcLivestreamController) {
        ctrl = vlcLivestreamController;
    }

    @Override
    public void _sendMessage(String channelname, String message) {
        ctrl._onMessage(new ChatMessage(message));
    }

    @Override
    public boolean _isConnected() {
        return false;
    }

    @Override
    public String getChannelName() {
        return "TEST";
    }

    @Override
    public String getUsername() {
        return "SampleUser";
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
