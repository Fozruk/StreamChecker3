package com.github.fozruk.streamcheckerguitest;

import org.apache.log4j.Logger;

import java.awt.*;

/**
 * Created by Philipp on 01.08.2015.
 */
public class BalloonTipManager {


    private static final Logger logger = Logger.getLogger(AddChannelForm.class);
    private TrayIcon trayIcon;
    private StringBuilder queuedStrings = new StringBuilder();
    private Thread activeThread;

    public BalloonTipManager(TrayIcon trayIcon)
    {
        this.trayIcon = trayIcon;
    }

    public synchronized void addMessageToQueue(String message)
    {
        queuedStrings.append(message + "\n");
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
                trayIcon.displayMessage("Info", builder.toString(), TrayIcon
                        .MessageType.INFO);


                BalloonTipManager.this.queuedStrings.setLength(0);
                BalloonTipManager.this.activeThread = null;
            }
        });

        activeThread.start();
    }
}
