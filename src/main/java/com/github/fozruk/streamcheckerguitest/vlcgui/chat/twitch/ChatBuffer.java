package com.github.fozruk.streamcheckerguitest.vlcgui.chat.twitch;

import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.Gui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Gui gui;
    private Date latest;


    public ChatBuffer(DefaultListModel<ChatMessage> model,Gui gui)
        {
            this.model = model;
            this.gui = gui;
            this.latest = new Date();
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
                double dreisatz = receivedMessages / (delay + timediff);
                double messagesPerSec = dreisatz * 1000;
                LOGGER.info("Messages per Second: " + messagesPerSec);
                gui.getTabbedPane().setTitleAt(0, messagesPerSec + " MPS " +
                        "(Timescale: " + timediff);
                temp.clear();
                latest = new Date();
                currentlyCollecting = false;
            }
        }
    }
}



