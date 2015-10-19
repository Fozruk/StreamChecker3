package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Philipp on 19.10.2015.
 */
class PopClickListener extends MouseAdapter {
    public PopClickListener() {

    }

    public void mousePressed(MouseEvent e){
        JList list = (JList)e.getSource();
        int row = list.locationToIndex(e.getPoint());
        list.setSelectedIndex(row);
        if (e.isPopupTrigger())
            doPop(e);
    }

    public void mouseReleased(MouseEvent e){
        if (e.isPopupTrigger())
            doPop(e);
    }

    private void doPop(MouseEvent e){
        ChatWindow list = (ChatWindow) e.getComponent();
        ChatMessage message = (ChatMessage) list.getModel().getElementAt(list
                .getSelectedIndex());
        CopyTextToClipboardmenu menu = new CopyTextToClipboardmenu(message);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}