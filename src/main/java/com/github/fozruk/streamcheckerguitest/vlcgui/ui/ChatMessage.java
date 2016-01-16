package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by Philipp on 13.08.2015.
 */
public class ChatMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatMessage.class);
    private String message;
    private String channelname;

    public void setColor(String color) {
        this.color = color;
    }

    private String color;
    private String username;
    int hour;
    int minute;

    public String getUsername() {
        return username;
    }

    public String getColor() {
        if(color == null || color.equals(""))
            color = "#3db974";
       return color;
    }

    public String getChannelName()
    {
        return channelname;
    }


    public String getMessage() {
        if(message == null)
            message = "NULL";
        return message;
    }

    public String getTime()
    {

        return String.format("[%02d", hour) +":"+String
                .format("%02d", minute) +"]";
    }

    public ChatMessage(String raw)
    {
        Calendar rightNow = Calendar.getInstance();
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        minute = rightNow.get(Calendar.MINUTE);
        this.username = "Notification";
        this.message = StringEscapeUtils.escapeHtml4(raw);
    }

    public ChatMessage(String user, String message)
    {
        Calendar rightNow = Calendar.getInstance();
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        minute = rightNow.get(Calendar.MINUTE);
        this.username = user;
        this.message = StringEscapeUtils.escapeHtml4(message);;
    }

    public ChatMessage(String user,String color,String message)
    {
        Calendar rightNow = Calendar.getInstance();
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        minute = rightNow.get(Calendar.MINUTE);
        this.message = StringEscapeUtils.escapeHtml4(message);;
        this.color = color;
        this.username = user;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
