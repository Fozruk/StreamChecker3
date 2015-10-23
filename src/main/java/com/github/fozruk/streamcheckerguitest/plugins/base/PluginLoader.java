package com.github.fozruk.streamcheckerguitest.plugins.base;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;

/**
 * Created by Philipp on 05.10.2015.
 */
public interface PluginLoader {
    void create(IChannel name);
    Stream returnObject();
}
