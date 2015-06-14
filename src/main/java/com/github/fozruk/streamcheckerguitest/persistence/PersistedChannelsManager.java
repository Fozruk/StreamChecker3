package com.github.fozruk.streamcheckerguitest.persistence;

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
}
