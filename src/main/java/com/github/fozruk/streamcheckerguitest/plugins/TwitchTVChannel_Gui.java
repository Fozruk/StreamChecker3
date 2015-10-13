package com.github.fozruk.streamcheckerguitest.plugins;

import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.plugins.base.PluginLoader;
import com.github.fozruk.streamcheckerguitest.plugins.base.Stream;
import com.github.fozruk.streamcheckerguitest.chat.IChat;
import com.github.fozruk.streamcheckerguitest.chat.twitch.TwitchImplNew;
import com.github.fozruk.streamcheckerguitest.util.Util;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.VlcPlayer;
import org.json.JSONException;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

/**
 * Created by Philipp on 05.10.2015.
 */
public class TwitchTVChannel_Gui implements PluginLoader {

    Stream stream;


    @Override
    public void create(IChannel channel){
        IChat chat = null;
        try {
            chat = new TwitchImplNew(channel);
            stream = new Stream(channel,chat,new VlcPlayer());
            stream.quality = new String[]{"source"};
        } catch (IOException | IrcException | ReadingWebsiteFailedException |
                JSONException | PropertyKeyNotFoundException e) {
            Util.printExceptionToMessageDialog("OOPS",e);
        }
    }

    @Override
    public Stream returnObject() {
        return stream;
    }

    public static void main(String[] args)
    {
        System.out.println(TwitchTVChannel_Gui.class.getCanonicalName());
    }

}
