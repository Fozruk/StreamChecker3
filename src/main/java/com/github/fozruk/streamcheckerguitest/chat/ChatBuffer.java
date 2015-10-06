package com.github.fozruk.streamcheckerguitest.chat;

import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.StreamWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Philipp on 03.10.2015.
 */
public class ChatBuffer {

    volatile ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
    volatile ArrayList<ChatMessage> messages2 = new ArrayList<ChatMessage>();
    private boolean currentlyCollecting = false;
    private boolean block;
    DefaultListModel model;
    private int delay = 250;
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatBuffer.class);
    private StreamWindow streamListWindow;
    private Date latest;
    private MessageStats stats;
    private double max = 0;


    public ChatBuffer(DefaultListModel<ChatMessage> model,StreamWindow streamListWindow)
        {
            this.model = model;
            this.streamListWindow = streamListWindow;
            this.latest = new Date();
            this.stats = new MessageStats();
        }

        public void setDelay(int delay)
        {
            this.delay = delay;
        }


        public synchronized void addMessage(ChatMessage message) {
            if(!currentlyCollecting)
            {
                currentlyCollecting = true;
                new Thread(new CollectThread()).start();
            }
            getCurrentList().add(message);
        }

    private ArrayList<ChatMessage> getCurrentList()
    {
        if(block)
            return messages;
        return messages2;
    }

    class CollectThread implements Runnable
    {

        @Override
        public void run() {
            ArrayList<ChatMessage> temp = null;
            try {
                Thread.sleep(delay);
                temp = getCurrentList();
                block = !block;
                for(ChatMessage p : temp)
                {
                    try {
                        SwingUtilities.invokeLater(() -> model.addElement(p));
                    } catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                double receivedMessages = temp.size();
                double timediff = new Date().getTime() - latest.getTime();
                double dreisatz = receivedMessages / (timediff);
                double messagesPerSec = dreisatz * 1000;
                stats.addAverage(messagesPerSec);
                LOGGER.info("Messages per Second for this tick: " + new DecimalFormat("#.##").format(messagesPerSec));
                max = messagesPerSec > max ? messagesPerSec : max;
                streamListWindow.getTabbedPane().setTitleAt(0,   new DecimalFormat("#.##")
                        .format(stats.getAverage()) + " Avg - " +
                        new DecimalFormat("#.##").format(messagesPerSec) + " " +
                        " Tick " +  new DecimalFormat("#.##").format(max) + "" +
                        " max");
                temp.clear();
                latest = new Date();
                currentlyCollecting = false;
            }
        }
    }
}



