package me.keshav.alexaandminecraft;

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
        String speechText = "Tell me to spawn a mob!";

        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("AlexaAndMinecraft", speechText)
                .withReprompt(speechText)
                .build();
    }
}
