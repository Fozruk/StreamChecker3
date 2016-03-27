package com.github.fozruk.streamcheckerguitest.tests;

import com.github.epilepticz.streamchecker.exception.UpdateChannelException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannelobserver;

import java.net.URL;

/**
 * Created by Philipp on 04.10.2015.
 */
public class MockChannel implements IChannel {
    @Override
    public int getViewerAmount() {
        return 0;
    }

    @Override
    public String getChannelLink() {
        return "http://twitch.tv/riotgames";
    }

    @Override
    public URL getApiLink() {
        return null;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public String getUptime() {
        return "";
    }

    @Override
    public String getChannelName() {
        return "";
    }

    @Override
    public void refreshData() throws UpdateChannelException {

    }

    @Override
    public void addObserver(IChannelobserver observer) {

    }

    @Override
    public void removeObserver(IChannelobserver observer) {

    }

    @Override
    public String getGameTitle() {
        return null;
    }
}
