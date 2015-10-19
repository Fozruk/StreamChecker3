package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Created by Philipp on 19.10.2015.
 */
public class CopyTextToClipboardmenu extends JPopupMenu {
    JMenuItem nameToClipboard;
    JMenuItem testToClipboard;
    public CopyTextToClipboardmenu(ChatMessage label)
    {


        testToClipboard = new JMenuItem("Text to clipboard");
        testToClipboard.addActionListener((e) -> {
            toClipBoard(label.getMessage());
        });

        nameToClipboard = new JMenuItem("Name to clipboard");
        nameToClipboard.addActionListener((e) -> {
            toClipBoard(label.getUsername());
        });

        add(testToClipboard);
        add(nameToClipboard);
    }

    private void toClipBoard(String string)
    {
        StringSelection selection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }


}
