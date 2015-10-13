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
    private String channelname;
    private ChatObserver observer;

    /**
     * Constructs a PircBotX with the provided configuration.
     *
     * @param configuration
     */
    public PircBotXTwitch(Configuration configuration,String channelname
            ) {
        super(configuration);
        this.channelname = channelname;
    }

    public void shutdown()
    {
        super.shutdown();
    }



    public void  start()
    {
        Thread botThread;

        botThread = new Thread(() -> {
            try {
                startBot();
            } catch (IOException | IrcException e )
            {
                Util.printExceptionToMessageDialog("OOPS",e);
            }
        });

        botThread.setName("Chat " + channelname);
        botThread.start();
    }
}
