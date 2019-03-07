package com.doodle.smallSlackBot;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

// run the tests in this class only on stand alone mode since they're interrupting each other's static vars.
class BotTest {
    public static Bot bot;
    public static Slack slackInst = null;

    @BeforeEach
    void setUp() throws InterruptedException {
        bot = new Bot();
        bot.start();
        slackInst = bot.getSlack();
        Thread.sleep(20*1000);
    }

    @AfterEach
    void tearDown() {
        bot.terminate();
    }

    @Test
    void testDifferentChannels() throws IOException, SlackApiException, InterruptedException {
        sendMessage("CFGBVHSHE", "10", "UFGFSEFKK");
        sendMessage("CFJ5RA9T9", "70", "UFGFSEFKK");
        sendMessage("CFH10NS91", "40", "UFGFSEFKK");
        assertEquals("40.0", String.valueOf(User.getTotalAvg()) );
        assertEquals("40.0", String.valueOf(User.getAvgPerUser("hadar.hd")) );
    }

    @Test
    void testWithNonNumericMsgs() throws IOException, SlackApiException, InterruptedException {
        sendMessage("CFGBVHSHE", "1h0", "UFGFSEFKK");
        sendMessage("CFJ5RA9T9", "a70", "UFGFSEFKK");
        sendMessage("CFH10NS91", "50", "UFGFSEFKK");
        sendMessage("CFH10NS91", "30-", "UFGFSEFKK");
        assertEquals("50.0", String.valueOf(User.getTotalAvg()) );
        assertEquals("50.0", String.valueOf(User.getAvgPerUser("hadar.hd")) );
    }

    @Test
    void testDifferentUsers() throws IOException, SlackApiException, InterruptedException {
        sendMessage("CFGBVHSHE", "15", "UFGFSEFKK");
        sendMessage("CFGBVHSHE", "-10", "UFH1BV40K");
        sendMessage("CFGBVHSHE", "-1", "UFGFSEFKK");
        sendMessage("CFGBVHSHE", "101", "UFH1BV40K");
        sendMessage("CFGBVHSHE", "9.25", "UFJGSR6UW");
        assertEquals("22.85", String.valueOf(User.getTotalAvg()) );
        assertEquals("7.0", String.valueOf(User.getAvgPerUser("hadar.hd")) );
        assertEquals("45.5", String.valueOf(User.getAvgPerUser("danielohana300")) );
        assertEquals("9.25", String.valueOf(User.getAvgPerUser("tdanielov")) );
    }

    public static void sendMessage(String channelId, String msg, String userName) throws IOException, SlackApiException {
        ChatPostMessageRequest.ChatPostMessageRequestBuilder requestBuilder = ChatPostMessageRequest.builder()
                    .token(System.getenv("CLIENT_ACCESS_TOKEN"))
                    .channel(channelId)
                    .text(msg)
                    .username(userName)
                    .replyBroadcast(false);
        ChatPostMessageResponse postMessageResponse = slackInst.methods().chatPostMessage(requestBuilder.build());
        assertThat(postMessageResponse.isOk(), is(true));
    }
}