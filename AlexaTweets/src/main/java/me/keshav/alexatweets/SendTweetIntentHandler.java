package me.keshav.alexatweets;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.request.Predicates;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Map;
import java.util.Optional;

public class SendTweetIntentHandler implements IntentRequestHandler {

    private static final String accessToken = "4438159933-73eTsOc5o9SGpZ8ME8EQ3GtW5OiKgroRAii0nPF";
    private static final String tokenSecret = "tjIWF0lFG7fVfuVY2icJspViop7SgJ9H1BegAH8WsRCJl";
    private static final String consumerKey = "2enc19pkEpmKfPF5Hyvz2TrPu";
    private static final String secretKey = "F8XY1I4kTQp3owqSBkculdXk7QyiseX4NcV5R4qPEKGj1meXZM";

    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return handlerInput.matches(Predicates.intentName("SendTweetIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

        Map<String, Slot> slots = intentRequest.getIntent().getSlots();
        Slot msgSlot = slots.get("message");
        String msg = msgSlot.getValue();
        StatusUpdate tweet = new StatusUpdate(msg + "\n - sent from Alexa");
        String speechText = "Status updated with message - " + msg;

        try {
            Twitter twitter = getTwitter();
            twitter.updateStatus(tweet);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("AlexaTweets", speechText)
                .build();
    }

    public Twitter getTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(secretKey)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(tokenSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        twitter.setOAuthAccessToken(new AccessToken(accessToken, tokenSecret));
        return twitter;
    }
}
