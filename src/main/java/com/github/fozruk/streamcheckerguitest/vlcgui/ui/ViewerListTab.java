package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by Philipp on 25.09.2015.
 */
public class ViewerListTab extends ResizeableList {
    private static final Logger LOGGER = LoggerFactory.getLogger
            (ViewerListTab.class);

    public ViewerListTab(DefaultListModel model) {
        super(model);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                LOGGER.debug("Viewerlist shown");
            }
        });
    }


}
