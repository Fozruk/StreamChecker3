package com.github.fozruk.streamcheckerguitest;

import com.github.epilepticz.streamchecker.exception.NoSuchChannelViewInOverviewException;
import com.github.epilepticz.streamchecker.model.channel.interf.IChannel;
import com.github.epilepticz.streamchecker.view.interf.IOverview;
/**
 * Created by Philipp on 22.05.2015.
 */
public class StreamCheckerControllerImpl implements IOverview {
    @Override
    public void addChannel(IChannel channel) {

    }

    @Override
    public void updateDataInChannelViewFor(IChannel channel) {

    }

    @Override
    public void deleteChannelViewFor(IChannel channel) throws NoSuchChannelViewInOverviewException {

    }

    @Override
    public IChannel[] getAddedChannels() {
        return new IChannel[0];
    }

    @Override
    public void errorCodeChangedFor(IChannel channel, int errorcount) {

    }
}
