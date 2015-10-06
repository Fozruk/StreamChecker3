package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by Philipp on 25.09.2015.
 */
public class ViewerList extends ResizeableList {
    private static final Logger LOGGER = LoggerFactory.getLogger
            (ViewerList.class);

    public ViewerList(DefaultListModel model) {
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
