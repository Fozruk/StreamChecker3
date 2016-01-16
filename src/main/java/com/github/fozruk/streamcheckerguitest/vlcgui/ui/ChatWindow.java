package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import com.github.fozruk.streamcheckerguitest.chat.chatstatistic.ChatStatisticEventListener;
import com.github.fozruk.streamcheckerguitest.chat.chatstatistic.ChatStatistic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.Formatter;

/**
 * Created by Philipp on 04.10.2015.
 */
public class ChatWindow extends ResizeableList implements ChatStatisticEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWindow.class);
    private String statisticString = "Avg - %.2f Tick - %.2f Max - %.2f";
    private double max;
    private double avg;
    private double now;

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

    @Override
    public void onChatStatisticEvent(ChatStatistic statistic) {
        Formatter formatter = new Formatter();
        formatter.format(this.statisticString,statistic.getAverage(),statistic.getThisTick(),statistic.getMax());
        JTabbedPane pane = (JTabbedPane)SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
        pane.setTitleAt(0,formatter.toString());
    }

}
