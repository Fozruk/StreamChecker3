package com.github.fozruk.streamcheckerguitest.vlcgui.controller;



import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.exception.UpdateChannelException;
import com.github.epilepticz.streamchecker.model.channel.impl.HitboxTVChannel;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.StreamGui.controller.Stream;
import com.github.fozruk.streamcheckerguitest.StreamGui.controller.TwitchPlugin;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.github.fozruk.streamcheckerguitest.plugin.PluginLoader;
import com.github.fozruk.streamcheckerguitest.tests.TestChannel;
import com.github.fozruk.streamcheckerguitest.tests.TestChatImpl;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.*;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.twitch.TwitchImplNew;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.StreamWindow;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.VlcPlayer;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import org.pircbotx.exception.IrcException;

public class VlcLivestreamController implements ChatObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger
            (VlcLivestreamController.class);
    private StreamWindow streamWindow;
    private PersistedSettingsManager persistenceManager =
            PersistedSettingsManager.getInstance();
    private boolean isLoaded;
    private boolean shutdownRequest;

    private Stream stream;

    public static final MessageHighlighter highligter = new
            MessageHighlighter(new ArrayList(Arrays.asList("f0zruk","fozruk")));
    private boolean loaded;

    public VlcLivestreamController(IChannel channel) throws IOException, IrcException, PropertyKeyNotFoundException, ReadingWebsiteFailedException, CreateChannelException, JSONException {
        this.streamWindow = new StreamWindow(this);
        channel.addObserver(streamWindow);

        PluginLoader loader = new TwitchPlugin();
        loader.create(channel);
        this.stream = loader.returnObject();

        startPlayer();
        startChat();

        //Refreshes Data for the frame title
        try {
            channel.refreshData();
        } catch (UpdateChannelException e) {
            e.printStackTrace();
        }
        this.loaded = true;
        if(shutdownRequest)
            this.stopWindow();
    }

    private void startPlayer() throws IOException {
        stream.getPlayer().setCanvas(this.streamWindow.getVlcPlayerCanvas());
        stream.getPlayer().play(new URL(stream.getChannel().getChannelLink()),
                stream.quality[0]);
    }

    private void startChat() throws ReadingWebsiteFailedException, JSONException, IOException {
        stream.getChat().setObserver(this);
        stream.getChat().start();
    }

    //ResizeableList Stuffs

    @Override
    public void _onMessage(ChatMessage message) {
        streamWindow.appendChatMessage(message);
    }

    public void sendMessage(String message) {
        stream.getChat()._sendMessage(stream.getChannel().getChannelName(), message);
    }

    public String getUsername()
    {
        return stream.getChat().getUsername();
    }

    //Methods for Closing events

    @Override
    public void _onDisconnect() {

    }

    public void stopWindow() {

        if(loaded)
        {
            stream.getPlayer().onShutdown(0);
            stream.getChat().disconnect();
        } else
        {
            LOGGER.info("Shutdown Request detected, gonna stop all processes " +
                    "if window is loaded complemetely.");
            shutdownRequest = true;
        }
        //chat._leaveChannel((TwitchTVChannel) channel);

    }

    //Player stuffs

    public void setVolume(int volume)
    {
        stream.getPlayer().setVolume(volume);
    }

    public void toggleFullscreen()
    {
        stream.getPlayer().toggleFullScreen();
    }


    public String[] reloadViewerList() throws ReadingWebsiteFailedException, JSONException, MalformedURLException {
        return stream.getChat().getUserList();
    }

    public void setFullscreen()
    {
        stream.getPlayer().toggleFullScreen();
    }

    public StreamWindow getStreamWindow()
    {
        return this.streamWindow;
    }

}
