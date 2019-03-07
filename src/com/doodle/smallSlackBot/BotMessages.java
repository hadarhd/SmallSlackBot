package com.doodle.smallSlackBot;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.*;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public final class BotMessages {
    private BotMessages(){}

    static void responseWithAvg(Slack slack, String token, String channelId, String text) {
        // https://slack.com/api/chat.postMessage
        ChatPostMessageResponse postResponse = null;
        try {
            postResponse = slack.methods().chatPostMessage(
                    ChatPostMessageRequest.builder()
                            .token(token)
                            .channel(channelId)
                            .text(text)
                            .build());
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }
        assertThat(postResponse.isOk(), is(true));
    }

    static void createHandlerPerNumericMsgs (JsonObject json, Pattern regexForNumbers, Slack slack, String token) {
        String msg = (json.get("type") != null) ? json.get("type").getAsString() : null;
        String username = json.get("username") != null ? json.get("username").getAsString() : null;
        if (msg != null && msg.equals("message") &&
                ((username == null) || (!username.equals("small bot in Slack for Doodle")))) {
            String text = json.get("text").getAsString();
            Matcher match = regexForNumbers.matcher(text);
            if (match.find()) {
                // find the correct channel to respond to
                String channelId = json.get("channel").getAsString();
                String userId = json.get("user")!= null ? json.get("user").getAsString() : (username);
                User user = User.usersAvgs.get(userId);
                if (user == null) {
                    user = new User (userId);
                }
                User.usersAvgs.put(userId, user.addNewNumberToAvg(Double.parseDouble(text)));
                responseWithAvg(slack, token, channelId, Double.toString(user.getUserCurrAvg()));
            }
        }
    }
}
