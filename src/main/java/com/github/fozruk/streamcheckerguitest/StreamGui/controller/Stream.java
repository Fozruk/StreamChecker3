package com.github.fozruk.streamcheckerguitest.StreamGui.controller;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.ChatObserver;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.IChat;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.VlcPlayer;

/**
 * Created by Philipp on 05.10.2015.
 */
public  class Stream implements ChatObserver {

    private IChannel channel;
    private IChat chat;

    public VlcPlayer getPlayer() {
        return player;
    }

    private VlcPlayer player;
    public String[] quality;

    public Stream(IChannel channel, IChat chat,VlcPlayer player)
    {
        this.channel = channel;
        this.chat = chat;
        this.player = player;
    }

    public IChannel getChannel() {
        return channel;
    }

    public void setChannel(IChannel channel) {
        this.channel = channel;
    }

    public IChat getChat() {
        return chat;
    }

    public void setChat(IChat chat) {
        this.chat = chat;
    }

    @Override
    public void _onMessage(ChatMessage message) {

    }

    @Override
    public void _onDisconnect() {

    }
}
