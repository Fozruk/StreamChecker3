package com.github.fozruk.streamcheckerguitest.plugin;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.IChat;

/**
 * Created by Philipp on 05.10.2015.
 */
public  class IStreamComponent {
    private IChannel channel;
    private IChat chat;

    private final String identifier;

    public IStreamComponent(String identifier)
    {
        this.identifier = identifier;
    }

    public IChat getChat() {
        return chat;
    }
    public IChannel getChannel() {
        return channel;
    }

    public String getIdentifier() {
        return identifier;
    }
}
