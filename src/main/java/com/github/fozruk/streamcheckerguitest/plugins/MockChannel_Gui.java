package com.github.fozruk.streamcheckerguitest.plugins;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.chat.IChat;
import com.github.fozruk.streamcheckerguitest.chat.test.MockChatImpl;
import com.github.fozruk.streamcheckerguitest.plugins.base.PluginLoader;
import com.github.fozruk.streamcheckerguitest.plugins.base.Stream;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.VlcPlayer;

/**
 * Created by Philipp on 07.10.2015.
 */
public class MockChannel_Gui implements PluginLoader {

    private Stream stream;

    @Override
    public void create(IChannel channel) {
        IChat chat = null;
        VlcPlayer player = null;
        chat = new MockChatImpl();
        player = new VlcPlayer();
        stream = new Stream(channel,chat,player);
        stream.quality = new String[]{"source"};
    }

    @Override
    public Stream returnObject() {
        return stream;
    }
}
