package com.github.fozruk.streamcheckerguitest.vlcgui.controller;



import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.exception.UpdateChannelException;
import com.github.epilepticz.streamchecker.model.channel.impl.HitboxTVChannel;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.*;
import com.github.fozruk.streamcheckerguitest.vlcgui.chat.twitch.TwitchImplNew;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.Gui;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.VlcPlayer;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import org.pircbotx.exception.IrcException;

public class VlcLivestreamController implements ChatObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger
            (VlcLivestreamController.class);
    Gui streamWindow;
    IChannel channel;
    VlcPlayer player;
    public IChat chat;

    public static final MessageHighlighter highligter = new
            MessageHighlighter(new ArrayList(Arrays.asList("xd", "lol",
            "f0zruk")));

    public VlcLivestreamController(IChannel channel, File vlc, File livestreamer) throws IOException, IrcException {
        this.channel = channel;
        this.streamWindow = new Gui(this);
        channel.addObserver(streamWindow);
        this.player = new VlcPlayer(streamWindow.getVlcPlayerCanvas(),vlc,livestreamer);
        startPlayer(channel);

        //Refreshes Data for the frame title
        try {
            channel.refreshData();
        } catch (UpdateChannelException e) {
            e.printStackTrace();
        }
    }

    private void startPlayer(IChannel channel) throws IOException {
        if(channel instanceof TwitchTVChannel)
        {
            try {
                chat = new TwitchImplNew(channel,this);
            } catch (IrcException|JSONException|ReadingWebsiteFailedException e) {
                e.printStackTrace();
            }
            player.play(new URL(channel.getChannelLink()), "source");
        } else if(channel instanceof HitboxTVChannel)
        {
            player.play(new URL(channel.getChannelLink()), "best");
        } else
        {
            throw new IllegalStateException("Channel not yet implemented");
        }
    }

    //ResizeableList Stuffs

    @Override
    public void _onMessage(ChatMessage message) {
        streamWindow.appendChatMessage(message);
    }

    public void sendMessage(String message) {
        chat._sendMessage(channel.getChannelName(), message);
    }

    //Methods for Closing events

    @Override
    public void _onDisconnect() {

    }

    public void stopWindow() {
        //chat._leaveChannel((TwitchTVChannel) channel);
        player.onShutdown(0);
        chat.disconnect();
    }

    //Player stuffs

    public void setVolume(int volume)
    {
        player.setVolume(volume);
    }

    public void toggleFullscreen()
    {
        player.toggleFullScreen();
    }


    public String[] reloadViewerList() throws ReadingWebsiteFailedException, JSONException, MalformedURLException {
        return chat.getUserList();
    }
}
