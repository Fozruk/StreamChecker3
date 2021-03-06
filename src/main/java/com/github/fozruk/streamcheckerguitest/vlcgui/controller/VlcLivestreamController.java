package com.github.fozruk.streamcheckerguitest.vlcgui.controller;


import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.exception.UpdateChannelException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.chat.ChatObserver;
import com.github.fozruk.streamcheckerguitest.chat.MessageHighlighter;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.github.fozruk.streamcheckerguitest.plugins.base.PluginLoader;
import com.github.fozruk.streamcheckerguitest.plugins.base.Stream;
import com.github.fozruk.streamcheckerguitest.util.Util;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.StreamWindow;
import org.jibble.pircbot.IrcException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class VlcLivestreamController implements ChatObserver {

    public static final MessageHighlighter highligter = new
            MessageHighlighter(new ArrayList(Arrays.asList("f0zruk", "fozruk")));
    private static final Logger LOGGER = LoggerFactory.getLogger
            (VlcLivestreamController.class);
    private static final String PLUGIN_PACKAGE_PATH = "com.github.fozruk" +
            ".streamcheckerguitest.plugins.";
    private StreamWindow streamWindow;
    private PersistedSettingsManager persistenceManager;
    private boolean isLoaded;
    private boolean shutdownRequest;
    private Stream stream;
    private boolean loaded;
    private String bufferedMessage;

    public VlcLivestreamController(IChannel channel) throws
            PropertyKeyNotFoundException, CreateChannelException {
        PluginLoader loader = null;
        try {
            this.streamWindow = new StreamWindow(this);
            persistenceManager = PersistedSettingsManager.getInstance();
            loader = loadPlugin(channel);
            loader.create(channel);
            this.stream = loader.returnObject();
            stream.getChannel().refreshData();
            stream.getChannel().addObserver(streamWindow);
            streamWindow.getVlcPlayerCanvas().appendMessage("Init....");
            startPlayer();
            startChat();
            this.loaded = true;
        } catch (UpdateChannelException e) {
            Util.printExceptionToMessageDialog("Something is wrong with your Proxy/Internetconnection :< ", e);
        } catch (ClassNotFoundException | InstantiationException| IllegalAccessException e) {
            LOGGER.error("Tried to use Plugin which does not exist", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (shutdownRequest)
            this.stopWindow();
    }

    private PluginLoader loadPlugin(IChannel channel) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (PluginLoader) Class.forName(PLUGIN_PACKAGE_PATH + channel.getClass()
                .getSimpleName() +
                "_Gui")
                .newInstance();
    }

    private void startPlayer() throws IOException {
        stream.getPlayer().setCanvas(this.streamWindow.getVlcPlayerCanvas());
        try {
            stream.getPlayer().play(new URL(stream.getChannel().getChannelLink()),
                    stream.quality[0]);
        } catch (Exception e) {
           LOGGER.error(e.getMessage(),e);
            String[] strings = Util.getStacktraceAsStringArray(e);
            for(String s : strings)
                streamWindow.getVlcPlayerCanvas().appendMessage(s);
        } catch(UnsatisfiedLinkError e)
        {
            LOGGER.error(e.getMessage(),e);
            String[] strings = Util.getStacktraceAsStringArray(e);
            for(String s : strings)
                streamWindow.getVlcPlayerCanvas().appendMessage(s);
            streamWindow.getVlcPlayerCanvas().appendMessage("");
            streamWindow.getVlcPlayerCanvas().appendMessage("Installing the 64 Bit VLC Player might fix the problem.");
        }
    }

    public void restartLivestreamer() throws IOException, ReadingWebsiteFailedException, JSONException {
        stream.getPlayer().restart(new URL(stream.getChannel()
                .getChannelLink()),
                stream.quality[0]);

        stream.getChat().disconnect();
        startChat();
    }

    private void startChat() {
        if(stream.getChat() != null)
        {
            stream.getChat().setObserver(this);
            try {
                stream.getChat().start();
            } catch (IOException | ReadingWebsiteFailedException | JSONException e) {
                LOGGER.error("Error while starting chat",e);
                _onMessage(new ChatMessage("An Error occured while starting chat," +
                        " please restart and check log files"));
            }
        } else
        {
            LOGGER.warn("No Chat impl found");
        }

    }

    //ResizeableList Stuffs

    @Override
    public void _onMessage(ChatMessage message) {
        streamWindow.appendChatMessage(message);
    }

    public void sendMessage(String message) {
        if(message.length() != 0) {
            this.bufferedMessage = message;
            stream.getChat()._sendMessage(stream.getChannel().getChannelName(), message);
        }
    }

    public String getUsername() {
        return stream.getChat().getUsername();
    }

    //Methods for Closing events

    @Override
    public void _onDisconnect() {

    }

    public void stopWindow() {
        //TODO null checks to be sure to call both methods
        if (loaded) {
            stream.getPlayer().onShutdown(0);
            stream.getChat().disconnect();
        } else {
            LOGGER.info("Shutdown Request detected, gonna stop all processes " +
                    "if window is loaded complemetely.");
            shutdownRequest = true;
        }
        //chat._leaveChannel((TwitchTVChannel) channel);

    }

    //Player stuffs

    public void setVolume(int volume) {
        stream.getPlayer().setVolume(volume);
    }

    public void toggleFullscreen() {
        SwingUtilities.invokeLater(() -> streamWindow.setCanvasToFullscreen());
    }


    public String[] reloadViewerList() throws ReadingWebsiteFailedException, JSONException, MalformedURLException {
        return stream.getChat().getUserList();
    }

    public void setFullscreen() {
        stream.getPlayer().toggleFullScreen();
    }

    public StreamWindow getStreamWindow() {
        return this.streamWindow;
    }

}
