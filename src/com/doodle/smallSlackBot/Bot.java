package com.doodle.smallSlackBot;

import com.github.seratch.jslack.*;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsListRequest;
import com.github.seratch.jslack.api.methods.request.users.UsersListRequest;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsListResponse;
import com.github.seratch.jslack.api.methods.response.users.UsersListResponse;
import com.github.seratch.jslack.api.rtm.*;
import com.github.seratch.jslack.common.http.SlackHttpClient;
import com.google.gson.*;
import okhttp3.OkHttpClient;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.regex.Pattern;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Bot extends Thread{
    final public Slack slack = getSlack();
    public boolean keepBotAlive = true;
    final static String BOT_TOKEN = System.getenv("BOT_ACCESS_TOKEN");

    public void run() {
        try (RTMClient rtm = slack.rtm(BOT_TOKEN)) {
            // find all channels in the team
            ChannelsListResponse channelsResponse = slack.methods().channelsList(
                    ChannelsListRequest.builder().token(BOT_TOKEN).build());
            assertThat(channelsResponse.isOk(), is(true));
            Pattern regexForNumbers = Pattern.compile("^\\-?\\d+(\\.\\d+)?$");
            // initialize mapping between the users Slack names and IDs for routing to the user average page with the correct avg
            UsersListResponse usersListResponse = slack.methods().usersList(UsersListRequest.builder()
                    .token(BOT_TOKEN)
                    .build());
            List<com.github.seratch.jslack.api.model.User> users = usersListResponse.getMembers();
            for (com.github.seratch.jslack.api.model.User user : users) {
                User.usersNamesToIDs.put(user.getName(), user.getId());
            }

            RTMMessageHandler handler = (message) -> {
                JsonParser jsonParser = new JsonParser();
                JsonObject json = jsonParser.parse(message).getAsJsonObject();
                BotMessages.createHandlerPerNumericMsgs (json, regexForNumbers, slack, BOT_TOKEN);
            };

            rtm.addMessageHandler(handler);

            // must connect within 30 seconds after issuing wss endpoint
            try {
                rtm.connect();
                Timer timer = new Timer(true);
                timer.schedule(new printTotalAvg(slack, BOT_TOKEN, channelsResponse), 0, 60*1000);
                while (keepBotAlive) {}
                cleanBot(rtm, handler);
            } catch (DeploymentException e) {
                e.printStackTrace();
            }
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Bot bot=new Bot();
        bot.start();
    }

    private void cleanBot(RTMClient rtm, RTMMessageHandler handler) {
        rtm.removeMessageHandler(handler);
        User.cleanStaticVars();
    }

    public Slack getSlack() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        SlackHttpClient slackHttpClient = new SlackHttpClient(okHttpClient);
        return Slack.getInstance(slackHttpClient);
    }

    public void terminate() {
        keepBotAlive = false;
    }
}
