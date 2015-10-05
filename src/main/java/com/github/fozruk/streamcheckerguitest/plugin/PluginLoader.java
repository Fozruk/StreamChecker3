package com.github.fozruk.streamcheckerguitest.plugin;

import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.StreamGui.controller.Stream;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import org.json.JSONException;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

/**
 * Created by Philipp on 05.10.2015.
 */
public interface PluginLoader {
    public void create(IChannel name) throws CreateChannelException,
            JSONException, IrcException, ReadingWebsiteFailedException, IOException, PropertyKeyNotFoundException;
    Stream returnObject();
}
