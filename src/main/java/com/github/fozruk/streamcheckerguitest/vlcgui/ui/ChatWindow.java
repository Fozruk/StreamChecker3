package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import org.pircbotx.dcc.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Philipp on 04.10.2015.
 */
public class ChatWindow extends ResizeableList {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWindow.class);

    public ChatWindow(DefaultListModel model) {
        super(model);
        addScrollListener();

    }

    public ChatWindow(DefaultListModel model, ListCellRenderer cellRenderer) {
        super(model, cellRenderer);
        addScrollListener();
        addMouseListener(new PopClickListener());
    }
    private void addScrollListener()
    {
        ListDataListener listdataListener = new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                if (ChatWindow.this.getModel().getSize() > 200)
                {
                    DefaultListModel model = (DefaultListModel) ChatWindow.this.getModel();
                    SwingUtilities.invokeLater(()->model.removeElementAt(0));
                    SwingUtilities.invokeLater(()->model.removeElementAt(1));
                }
                /*LOGGER.debug("Chat element length " + ChatWindow.this
                        .getModel().getSize());*/
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        };
        DefaultListModel model = (DefaultListModel) ChatWindow.this.getModel();
        model.addListDataListener(listdataListener);
    }
}
