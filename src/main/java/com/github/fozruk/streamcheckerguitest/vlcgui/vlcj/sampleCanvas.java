package com.github.fozruk.streamcheckerguitest.vlcgui.vlcj;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Philipp on 14.08.2015.
 */
public class sampleCanvas extends Canvas {

    private String string = "Loading Video Stream.......";
    private ArrayList<String> strings = new ArrayList<>();

    public void livestreamerClosed()
    {
        string = "Livestreamer ended";
        revalidate();
        validate();
        repaint();
    }

    public void appendMessage(String message)
    {
        strings.add(message);
        revalidate();
        validate();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int i = 12;
        for(String st : strings)
        {
            string = st;
            g.drawString(string,12,i);
            i += 12;
        }
    }
}
