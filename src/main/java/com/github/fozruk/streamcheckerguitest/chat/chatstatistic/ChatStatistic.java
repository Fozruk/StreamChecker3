package com.github.fozruk.streamcheckerguitest.chat.chatstatistic;

/**
 * Created by philipp on 16.01.16.
 */
public class ChatStatistic {


    double max;
    double average;

    double thisTick;
    double timediff;

    public ChatStatistic(double receivedMessages, double timeDiff,double max, double average) {
        this.thisTick = receivedMessages;
        this.timediff = timeDiff;
        this.max = max;
        this.average = average;
    }

    public double getTimediff() {
        return timediff;
    }

    public double getThisTick() {
        return thisTick;
    }

    public double getAverage() {
        return average;
    }

    public double getMax() {
        return max;
    }

}
