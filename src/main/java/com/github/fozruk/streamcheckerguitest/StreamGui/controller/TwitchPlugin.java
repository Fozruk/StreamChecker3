package com.github.fozruk.streamcheckerguitest.StreamGui.controller;

import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.plugin.PluginLoader;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.IChat;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.twitch.TwitchImplNew;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.VlcPlayer;
import org.json.JSONException;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

/**
 * Created by Philipp on 05.10.2015.
 */
public class TwitchPlugin implements PluginLoader {

    Stream stream;


    @Override
    public void create(IChannel channel) throws CreateChannelException,
            JSONException,
            IrcException, ReadingWebsiteFailedException, IOException, PropertyKeyNotFoundException {

        IChat chat = new TwitchImplNew(channel,null);
        stream = new Stream(channel,chat,new VlcPlayer());
        stream.quality = new String[]{"source"};
    }

    @Override
    public Stream returnObject() {
        return stream;
    }
}
