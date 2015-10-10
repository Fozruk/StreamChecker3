package com.github.fozruk.streamcheckerguitest.plugins;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.chat.IChat;
import com.github.fozruk.streamcheckerguitest.chat.test.MockChat;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.plugins.base.PluginLoader;
import com.github.fozruk.streamcheckerguitest.plugins.base.Stream;
import com.github.fozruk.streamcheckerguitest.util.Util;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.VlcPlayer;

import java.io.IOException;

/**
 * Created by Philipp on 07.10.2015.
 */
public class MockChannel_Gui implements PluginLoader {

    private Stream stream;

    @Override
    public void create(IChannel channel) {
        IChat chat = null;
        try {
            chat = new MockChat();
            stream = new Stream(channel,chat,new VlcPlayer());
        } catch (IOException | PropertyKeyNotFoundException e) {
            Util.printExceptionToMessageDialog(e);
        }
        stream.quality = new String[]{"source"};
    }

    @Override
    public Stream returnObject() {
        return stream;
    }
}
