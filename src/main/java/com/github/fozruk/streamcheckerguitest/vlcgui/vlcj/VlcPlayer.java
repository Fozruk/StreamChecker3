package com.github.fozruk.streamcheckerguitest.vlcgui.vlcj;

import com.github.epilepticz.JavaLivestreamerWrapper.ILivestreamerObserver;
import com.github.epilepticz.JavaLivestreamerWrapper.IServerOberserver;
import com.github.epilepticz.JavaLivestreamerWrapper.ServerLivestreamer;
import com.github.epilepticz.JavaLivestreamerWrapper.SortOfMessage;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Philipp on 12.08.2015.
 */
public class VlcPlayer implements IServerOberserver,ILivestreamerObserver {

    sampleCanvas canvas;
    EmbeddedMediaPlayer player;
    ServerLivestreamer livestreamer;
    MediaPlayerFactory mediaPlayerFactory;
    boolean mediaIsPlaying;
    private PersistedSettingsManager persistenceManager;

    private  String LIVESTREAMERPATH;
    private  String VLCPATH;

    private static final Logger LOGGER = LoggerFactory.getLogger(VlcPlayer
            .class);

    /**
     * The vlcj direct rendering media player component.
     */
    private  EmbeddedMediaPlayerComponent mediaPlayerComponent;




    public VlcPlayer() {

    }

    public void setCanvas(sampleCanvas canvas)
    {
        this.canvas = canvas;
    }

    public void play(URL url,String quality) throws PropertyKeyNotFoundException, IOException {
        persistenceManager = PersistedSettingsManager.getInstance();
        this.LIVESTREAMERPATH = persistenceManager.getLivestreamerPath();
        this.VLCPATH =  persistenceManager.getVideoPlayerPath();
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), this
                .VLCPATH);

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        player = mediaPlayerComponent.getMediaPlayer();

        mediaPlayerFactory = new MediaPlayerFactory();



        this.livestreamer = new ServerLivestreamer(LIVESTREAMERPATH,VLCPATH,
                this);
        this.livestreamer.addObserver(this);

        this.livestreamer.startLiveStreamerAsServer(url, quality, this);
    }

    @Override
    public void onBroadcastServerip(String ip) {
        if(!mediaIsPlaying)
        {
            LOGGER.info("IP Found for VLC: " + ip);
            player.setVideoSurface(mediaPlayerFactory.newVideoSurface(canvas));
            player.playMedia(ip);
            mediaIsPlaying = true;
            canvas.appendMessage("Start VLC.....");
        }

    }

    @Override
    public void onShutdown(int exitcode) {
        mediaIsPlaying = false;
        livestreamer.killProcess();
        canvas.livestreamerClosed();
        player.stop();
    }

    public void restart(URL url,String quality)
    {
        livestreamer.killProcess();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        canvas.appendMessage("Restarting Livestreamer");
        livestreamer.startLiveStreamerAsServer(url,quality,this);
    }

    @Override
    public void onStreamEnded() {
        onShutdown(-1);
    }

    @Override
    public void recieveLivestreamerMessage(String message, SortOfMessage sort) {
        canvas.appendMessage(message);
    }

    public void setVolume(int volume)
    {
        player.setVolume(volume);
    }

    public void toggleFullScreen()
    {

    }
}
