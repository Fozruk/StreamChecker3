package com.github.fozruk.streamcheckerguitest.chat.twitch;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.chat.ChatObserver;
import com.github.fozruk.streamcheckerguitest.chat.IChat;
import com.github.fozruk.streamcheckerguitest.util.Util;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

/**
 * Created by Philipp on 19.08.2015.
 */
public class PircBotXTwitch extends PircBotX {

    private Configuration conf;
    private Thread botThread;
    private IServerCallback callback;
    private ChatObserver observer;

    /**
     * Constructs a PircBotX with the provided configuration.
     *
     * @param configuration
     */
    public PircBotXTwitch(Configuration configuration,IServerCallback
            callback) {
        super(configuration);
        this.callback = callback;
    }

    public void shutdown()
    {
        super.shutdown();
    }



    public void  start()
    {
        Thread botThread = new Thread();
        botThread.setName("test");
        botThread = new Thread(() -> {
            try {
                startBot();
            } catch (IOException | IrcException e )
            {
                Util.printExceptionToMessageDialog(e);
            }
        });

        botThread.setName("PircBotX-Thread: Connected to ");
        botThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Request these Commands to activate additional features like Whisper
        // Messages and extended chat messages
        sendRaw().rawLine("CAP REQ :twitch.tv/tags");
        sendRaw().rawLine("CAP REQ :twitch.tv/commands");
        if(callback != null)
            callback.connected();
    }
}
