package com.github.fozruk.streamcheckerguitest.plugin;

import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.exception.ReadingWebsiteFailedException;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.StreamGui.controller.Stream;
import com.github.fozruk.streamcheckerguitest.StreamGui.controller.TwitchPlugin;
import org.json.JSONException;
import org.pircbotx.exception.IrcException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Philipp on 05.10.2015.
 */
public class PluginManager {
    private Map<String,Class<IChannel>> livestreamplugins = new HashMap<>();

    public  PluginManager()
    {
        IStreamComponent comp = new IStreamComponent(TwitchTVChannel.class.getName());



    }

    public IChannel getChannel(IChannel name) throws
            ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ReadingWebsiteFailedException, CreateChannelException, IrcException, JSONException, IOException {


        IChannel xd  = (IChannel) Class.forName(name.getClass().getCanonicalName())
                .getConstructor(String
                        .class)
                .newInstance("k");

        PluginLoader xdd = (PluginLoader) Class.forName(TwitchPlugin.class
                        .getCanonicalName()
                ).newInstance();
        //xdd.create("xd");
        Stream xld = xdd.returnObject();

        return xd;

    }

    public void addPlugin()
    {

    }

    public static void main(String[] main) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CreateChannelException, JSONException, IrcException, ReadingWebsiteFailedException, IOException {
        IChannel xd =new PluginManager().getChannel(new TwitchTVChannel("xd"));

    }

}
