package me.keshav.alexatweets;


import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

import java.util.Optional;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(Predicates.requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        String speechText = "I do Twitter, tell twitter bot to update your status!";

        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("AlexaTweets", speechText)
                .withReprompt(speechText)
                .build();
    }
}
