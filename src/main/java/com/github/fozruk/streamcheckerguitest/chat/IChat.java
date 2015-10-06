package com.github.fozruk.streamcheckerguitest.chat;

import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;

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
    String[] getUserList() throws MalformedURLException,
            ReadingWebsiteFailedException, JSONException;

    void start() throws IOException, ReadingWebsiteFailedException, JSONException;
    void setObserver(ChatObserver observer);
}
