package com.github.fozruk.streamcheckerguitest;

import com.github.fozruk.streamcheckerguitest.streamlistgui.ui.AddChannelForm;
import com.github.fozruk.streamcheckerguitest.persistence.PersistedSettingsManager;
import com.github.fozruk.streamcheckerguitest.persistence.PersistenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.TrayIcon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Philipp on 01.08.2015.
 */
public class BalloonTipManager {


    private static final Logger logger = LoggerFactory.getLogger(AddChannelForm
            .class);
    private TrayIcon trayIcon;
    private StringBuilder queuedStrings = new StringBuilder();
    private Thread activeThread;


    public BalloonTipManager(TrayIcon trayIcon)
    {
        this.trayIcon = trayIcon;
    }

    public synchronized void addMessageToQueue(String message, boolean isOnline)
    {
        queuedStrings.append(message + ":" + isOnline + "\n");
        logger.debug("New String added to Queue: " + message);
        if(activeThread == null)
            showMessage();
    }

    //Some help maybe https://gist.github.com/jewelsea/e231e89e8d36ef4e5d8a
    private void showMessage() {
        //javax.swing.SwingUtilities.invokeLater(() -> trayIcon.displayMessage("h", "k", TrayIcon.MessageType.ERROR));


        this.activeThread = new Thread(new Runnable() {


            public void run() {

                int collectionTimeout = 5;
                logger.debug("Collection Thread started, wait " +
                        collectionTimeout + " Seconds for more Messages");

                StringBuilder builder = new StringBuilder(queuedStrings.toString());
                while(collectionTimeout > 0)
                {
                    try {
                        collectionTimeout--;
                        if(builder.length() != queuedStrings.length())
                        {
                            builder = queuedStrings;
                            collectionTimeout = 2;

                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



                logger.debug("Gonna dispatch messages....");

                try {
                    if(PersistedSettingsManager.getInstance().getOs() == PersistenceManager.OperatingSystem.Windows)
                    {
                        trayIcon.displayMessage("Info", builder.toString(), TrayIcon.MessageType.INFO);

                    } else if(PersistedSettingsManager.getInstance().getOs() == PersistenceManager.OperatingSystem.Linux)
                    {
                        /*for(String xd : queuedStrings.toString().split("\n"))
                        {
                            //String temp = "/usr/bin/notify-send 'Info' '"+xd+"' --icon=dialog-information";
                            String[] cmd = { "/usr/bin/notify-send",
                                    "-t",
                                    "10000",
                                    xd,
                            "--icon=dialog-information"};
                            Runtime.getRuntime().exec(cmd);
                        }*/

                        String[] cmd = { "/usr/bin/notify-send",
                                "-t",
                                "10000",
                                "Streamchecker Info",
                                generateMessage(queuedStrings.toString()),
                                "--icon=dialog-information"};
                        Runtime.getRuntime().exec(cmd);


                    }else if(PersistedSettingsManager.getInstance().getOs() == PersistenceManager.OperatingSystem.Mac)
                    {
                        String queued = "Channel update:\n "+ queuedStrings.toString().replace("\n"," ");

                            //String temp = "/usr/bin/notify-send 'Info' '"+xd+"' --icon=dialog-information";
                            String[] cmd = { "terminal-notifier",
                                    "-message",
                                    queued,
                                    "-title",
                                    "Info"};

                            Runtime.getRuntime().exec(cmd);

                        //terminal-notifier -message 'hi' -title 'hi'
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                BalloonTipManager.this.queuedStrings.setLength(0);
                BalloonTipManager.this.activeThread = null;
            }
        });

        activeThread.setName("BalloonTipThread");
        activeThread.start();
    }

    private String generateMessage(String string)
    {
        List<String> onlineChannels= new ArrayList<>();
        List<String> offlineChannels = new ArrayList<>();

        String[] channels = string.split("\n");
        Arrays.stream(channels).forEach((channel) -> {
            String[] temp = channel.split(":");
            if(temp[1].equals("false"))
                offlineChannels.add(temp[0]);
            else
                onlineChannels.add(temp[0]);
        });

        StringBuilder builder = new StringBuilder();
        if(onlineChannels.size() > 0)
        {
            builder.append("Online \n");
            onlineChannels.forEach((channel) -> builder.append("\\t" + channel + "\n"));
        }

        //todo remove last newline
        if(offlineChannels.size() > 0)
        {
            builder.append("Offline \n");
            offlineChannels.forEach((channel) -> builder.append("\\t" +channel + "\n" ));
        }


        return builder.toString();
    }
}
