package me.keshav.alexaandminecraft;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.request.Predicates;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import java.util.Map;
import java.util.Optional;

public class SpawnMobIntentHandler implements IntentRequestHandler {
    @Override
    public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {
        return handlerInput.matches(Predicates.intentName("SpawnMobIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

        Map<String, Slot> slots = intentRequest.getIntent().getSlots();
        Slot mobSlot = slots.get("mob");
        String mob = mobSlot.getValue();
        String speechText = mob + " spawned!";

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Minecraft_Signal");
        Item item = new Item()
                .withPrimaryKey("SignalId", mob);
        table.putItem(item);

        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("AlexaAndMinecraft", speechText)
                .build();
    }
}
