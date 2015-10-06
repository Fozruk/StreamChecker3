package com.github.fozruk.streamcheckerguitest.plugins;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.exception.PropertyKeyNotFoundException;
import com.github.fozruk.streamcheckerguitest.plugins.base.PluginLoader;
import com.github.fozruk.streamcheckerguitest.plugins.base.Stream;
import com.github.fozruk.streamcheckerguitest.vlcgui.vlcj.VlcPlayer;

import java.io.IOException;

/**
 * Created by Philipp on 06.10.2015.
 */
public class HitboxTVChannel_Gui implements PluginLoader {

    Stream stream;

    @Override
    public void create(IChannel channel){
        try {
            stream = new Stream(channel,null,new VlcPlayer());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyKeyNotFoundException e) {
            e.printStackTrace();
        }

        stream.quality = new String[]{"best"};
    }

    @Override
    public Stream returnObject() {
        return stream;
    }
}
