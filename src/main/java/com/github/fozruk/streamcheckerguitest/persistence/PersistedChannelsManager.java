package com.github.fozruk.streamcheckerguitest.persistence;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.streamlistgui.controller.Controller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Philipp on 14.06.2015.
 */
public class PersistedChannelsManager extends PersistenceManager {


    public PersistedChannelsManager() throws IOException {

    }

    public List<String> getPersistedChannels() throws IOException {
        List<String> channelList = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(STREAMS_FILE));
            String streamURL = null;

            while ((streamURL = reader.readLine()) != null) {
                channelList.add(streamURL);
            }
        } finally {
            reader.close();
        }
        return channelList;
    }

    public void saveChannel(IChannel channel) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(STREAMS_FILE, true));
            writer.write(channel.getChannelLink() + "\n");
            writer.flush();
            writer.close();
        } finally {
            writer.close();
        }
    }

    public void saveChannelwithJson() throws IOException {
        JSONObject persistedchannels = new JSONObject();
        Map<String,JSONArray> channels = new HashMap<>();
        IChannel[] currentlyDisplayedChannels = Controller.getCurrentController().getAddedChannels();
        for(IChannel channel : currentlyDisplayedChannels)
        {
            String classpath = channel.getClass().getCanonicalName();
            JSONArray array = channels.get(classpath);
            if(array == null)
            {
                channels.put(classpath,new JSONArray());
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

        FileWriter file = new FileWriter(STREAMS_FILE);
        try {
            file.write(persistedchannels.toString());

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            file.flush();
            file.close();
        }
    }

    public JSONObject loadPersistedChannels() throws IOException, JSONException {
        String text = new String(Files.readAllBytes(Paths.get(STREAMS_FILE.getAbsolutePath())), StandardCharsets.UTF_8);
        return new JSONObject(text);
    }

    /**
     * Writes down the entire list of streams that are currently displayed, you have to call the controllers delete method before this method
     *
     * @throws IOException
     */
    public void deleteChannel() throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(STREAMS_FILE));
            for (IChannel channel : Controller.getCurrentController().getAddedChannels()) {
                writer.write(channel.getChannelLink() + "\n");
                writer.flush();
            }
        } finally {
            writer.close();
        }
    }
}
