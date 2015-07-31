package com.github.fozruk.streamcheckerguitest;

import org.apache.log4j.Logger;

import java.awt.*;
import java.io.IOException;

/**
 * Created by Philipp on 22.05.2015.
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IOException, AWTException {
        LOG.info("Stramchecker started");
        MainWindow.startMainWindow();
        LOG.info("Streamchecker closed");
    }

}
