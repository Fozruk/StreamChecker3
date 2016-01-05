package com.github.fozruk.streamcheckerguitest.chat.test;

import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.fozruk.streamcheckerguitest.chat.ChatObserver;
import com.github.fozruk.streamcheckerguitest.chat.IChat;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;

/**
 * Created by philipp.hentschel on 23.10.15.
 */
public class MockChatImpl implements IChat {
    private ChatObserver observer;
    private Thread thread;
    private boolean running;
    private NonsenseGenerator gen = new NonsenseGenerator();

    @Override
    public void _sendMessage(String channelname, String message) {
        this.observer._onMessage(new ChatMessage("test test test " +
                "23234234234234"));
    }

    @Override
    public boolean _isConnected() {
        return false;
    }

    @Override
    public String getChannelName() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void disconnect() {
        running = false;
    }

    @Override
    public String[] getUserList() throws MalformedURLException, ReadingWebsiteFailedException, JSONException {
        return new String[0];
    }

    @Override
    public void start() throws IOException, ReadingWebsiteFailedException, JSONException {

        running = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(running)
                {
                    int randomnumber = 2 + new Random().nextInt(5);
                    try {
                        Thread.sleep(randomnumber*2000);
                        observer._onMessage(new ChatMessage("User",gen.makeText(randomnumber)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();


    }

    @Override
    public void setObserver(ChatObserver observer) {
        this.observer = observer;
    }
}


