package com.github.fozruk.streamcheckerguitest.vlcgui.ui;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Philipp on 13.08.2015.
 */
public class ChatMessage {


    private static final Logger LOGGER = LoggerFactory.getLogger(ChatMessage.class);
    private String message;
    private String channelname;
    private String color;
    private String username;
    Map<String,String> mappedString;
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

    String getTime()
    {

        return String.format("[%02d", hour) +":"+String
                .format("%02d", minute) +"]";
    }


    public ChatMessage(String rawString)
    {
        Calendar rightNow = Calendar.getInstance();
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        minute = rightNow.get(Calendar.MINUTE);

        mappedString = new HashMap<>();
        if(rawString.contains(";")) {


            String[] values = rawString.split(";");
            for (String temp : values) {
                String key = temp.substring(0, temp.indexOf("="));
                String value = org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(temp
                        .substring(temp
                                .indexOf("=") + 1));
                mappedString.put(key, value);
            }
        }

        if(rawString.startsWith("@color") || rawString.startsWith
                ("line=@color"))
        {
            String string= mappedString.get("display-name");
            String usertype = mappedString.get("user-type");
            //LOGGER.info("DisplayName: " + string);
            if(string == null) {
                // LOGGER.info("Inside Null Check");
                if(usertype != null) {
                    // LOGGER.info("Usertype: " + usertype);
                    string = usertype.substring(usertype.indexOf(":"), usertype
                            .indexOf("!"));
                }
                else
                {
                    string = "NULL";
                }


            }

            String whisperString = usertype.split(" ")[2];
            if(whisperString.contains("WHISPER"))
            {
                string = string + " > " + usertype.split(" ")[3];
            }

            username = org.apache.commons.lang3.StringEscapeUtils.escapeHtml4
                    (string);



             this.color =  mappedString.get("@color");
            // LOGGER.info("String: " + string);

            String temp = mappedString.get("user-type");
            channelname = temp.split(" ")[3];

            String temp2 = mappedString.get("user-type");
            message =  temp2.substring(temp2.indexOf(":", temp2.indexOf(":")+1)
                    +1);

        } else if(rawString.startsWith("@broadcaster"))
        {
            this.username = "Info-Twitch";
            this.message = rawString;
        } else if(rawString.startsWith("@msg-id"))
        {
            this.username = "Info-Twitch";
            this.message = rawString;
        } else
        {
            this.username = "Info-Twitch";
            this.message = rawString;
        }
    }

    public ChatMessage(String user, String message)
    {
        Calendar rightNow = Calendar.getInstance();
        hour = rightNow.get(Calendar.HOUR_OF_DAY);
        minute = rightNow.get(Calendar.MINUTE);
        this.username = user;
        this.message = message;
    }

}
