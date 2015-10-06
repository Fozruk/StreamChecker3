package com.github.fozruk.streamcheckerguitest.chat.twitch;

import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp on 03.10.2015.
 */
public class MessageStats {

    List<Double> values = new ArrayList<>();
    private double average;

    public void addAverage(double average)
    {
        if(values.size() > 100)
        {
            values.remove(0);
        }
        values.add(average);
    }


    public double getAverage()
    {
        computeAverage();
        return average;
    }

    private void computeAverage()
    {
        average = 0;
        for(double temp : values)
        {
            average += temp;
        }
        average = average / (double) values.size();
    }
}
