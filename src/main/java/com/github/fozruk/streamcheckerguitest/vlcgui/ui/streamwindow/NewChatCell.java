package com.github.fozruk.streamcheckerguitest.vlcgui.ui.streamwindow;

import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatCell;
import com.github.fozruk.streamcheckerguitest.vlcgui.ui.ChatMessage;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Philipp on 04.10.2015.
 */
public class NewChatCell extends JPanel{

    JLabel username = new JLabel();
    ChatCell message = new ChatCell();

    public NewChatCell(ChatMessage chatMessage)
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(username);
        add(message);

        username.setText(chatMessage.getTime() + chatMessage.getUsername());
        username.setForeground(Color.decode(chatMessage.getColor()));
        message.setText(chatMessage.getMessage());
    }
}
