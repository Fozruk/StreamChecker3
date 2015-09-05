package com.github.fozruk.streamcheckerguitest.vlcgui.chat.twitch;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

/**
 * Created by Philipp on 19.08.2015.
 */
public class PircBotXTwitch extends PircBotX {
    /**
     * Constructs a PircBotX with the provided configuration.
     *
     * @param configuration
     */
    public PircBotXTwitch(Configuration<? extends PircBotX> configuration) {
        super(configuration);
    }

    public void shutdown()
    {
        shutdown(true);
    }
}
