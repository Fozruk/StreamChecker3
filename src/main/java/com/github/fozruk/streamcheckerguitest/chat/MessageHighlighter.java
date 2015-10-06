package com.github.fozruk.streamcheckerguitest.chat;

import java.util.ArrayList;

/**
 * Created by Philipp on 14.08.2015.
 *
 * A Class which holds a list of keywords that shall be highlited if someone
 * writes it in chat.
 */
public class MessageHighlighter {

    private ArrayList<String> notifyList = new ArrayList<>();

    public MessageHighlighter(ArrayList keywords)
    {
        notifyList.addAll(keywords);
    }

    public void addToNotifyList(String keyWord)
    {
        this.notifyList.add(keyWord.toLowerCase());
    }

    public void removeFromNotifyList(String keyWord)
    {
        this.notifyList.remove(keyWord);
    }

    public boolean shallBeHighlighted(String string)
    {
        for(String st : notifyList)
        {
            if (string.toLowerCase().contains(st))
                return true;
        }
        return false;
    }

}
