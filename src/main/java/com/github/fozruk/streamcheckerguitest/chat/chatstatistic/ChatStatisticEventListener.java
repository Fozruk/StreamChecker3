package com.github.fozruk.streamcheckerguitest.chat.chatstatistic;

import com.github.fozruk.streamcheckerguitest.chat.chatstatistic.ChatStatistic;

/**
 * Created by philipp on 16.01.16.
 */
public interface ChatStatisticEventListener {
    void onChatStatisticEvent(ChatStatistic statistic);
}
