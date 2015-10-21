package com.github.fozruk.streamcheckerguitest.tests;

import com.github.epilepticz.streamchecker.controller.StreamcheckerController;
import com.github.epilepticz.streamchecker.exception.CreateChannelException;
import com.github.epilepticz.streamchecker.exception.NoSuchChannelViewInOverviewException;
import com.github.epilepticz.streamchecker.model.channel.impl.HitboxTVChannel;
import com.github.epilepticz.streamchecker.model.channel.impl.TwitchTVChannel;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.view.interf.IOverview;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedChannelsManager;
import com.github.fozruk.streamcheckerguitest.persistence.PersistenceManager;
import com.github.fozruk.streamcheckerguitest.streamlistgui.controller.Controller;
import junit.framework.TestCase;
import junit.framework.TestResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by philipp.hentschel on 21.10.15.
 */
public class JSONTest extends TestCase {

    @Test
    public void testJSON() throws IOException, CreateChannelException {
        JSONObject persistedchannels = new JSONObject();
        Map<String,JSONArray> channels = new HashMap<>();
        IChannel[] currentlyDisplayedChannels = {new HitboxTVChannel("xd"),new TwitchTVChannel("xddd"),new HitboxTVChannel("jknkjn"),new TwitchTVChannel("kjnkjnkjnkjnkjn")};
        for(IChannel channel : currentlyDisplayedChannels)
        {
            String classpath = channel.getClass().getCanonicalName();
            JSONArray array = channels.get(classpath);
            if(array == null)
            {
                channels.put(classpath,array = new JSONArray());
            }
            array.put(channel.getChannelName());
        }
        channels.forEach((string,array) -> {
            try {
                persistedchannels.put(string,array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        FileWriter file = new FileWriter("test");
        try {
            file.write(persistedchannels.toString(3));

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }
}
