package com.doodle.smallSlackBot;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsListResponse;
import com.github.seratch.jslack.api.model.Channel;
import java.util.TimerTask;
import static com.doodle.smallSlackBot.BotMessages.responseWithAvg;
import static com.doodle.smallSlackBot.User.*;

public class printTotalAvg extends TimerTask {
    private final Slack slack;
    private final String token;
    private final ChannelsListResponse channesList;

    private printTotalAvg(){
        this.slack = null;
        this.token = "";
        this.channesList = null;
    }
    public printTotalAvg (Slack slack, String token, ChannelsListResponse channesList) {
        this.slack = slack;
        this.token = token;
        this.channesList = channesList;
    }
    @Override
    public void run() {
        if (User.getAmountOfMsgInLastPrint() != getTotalAmountOfMsgs()) {
            setAmountOfMsgInLastPrint(getTotalAmountOfMsgs());
            for (Object c : channesList.getChannels()) {
                Channel channel = (Channel) c;
                responseWithAvg(slack, token,  channel.getId(), Double.toString(User.getTotalAvg()));
            }
        }
    }
}
