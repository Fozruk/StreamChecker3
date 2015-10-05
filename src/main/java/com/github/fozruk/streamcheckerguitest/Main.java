package com.github.fozruk.streamcheckerguitest;

import com.github.fozruk.streamcheckerguitest.StreamGui.ui.StreamListUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import sun.util.logging.PlatformLogger;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Philipp on 22.05.2015.
 */
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, AWTException {

        //com.sun.javafx.Logging.getCSSLogger().setLevel(PlatformLogger.Level.OFF);

        LOG.info("JAVAFX Version: " + com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
        LOG.info("Stramchecker started");

        StreamListUI.startMainWindow();
        LOG.info("Streamchecker closed");
    }

}
