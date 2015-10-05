package com.github.fozruk.streamcheckerguitest.persistence;

import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.fozruk.streamcheckerguitest.StreamGui.controller.Controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
