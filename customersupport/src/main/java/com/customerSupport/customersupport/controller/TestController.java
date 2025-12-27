package com.customerSupport.customersupport.controller;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController

@RequestMapping("/chat")
@RequiredArgsConstructor // it generates a constructor that takes a ChatClient parameter, enabling dependency injection.
public class TestController {

    private final ChatClient chatClient;

    public String chatWithAi(@RequestParam String message){

        List<Message>  messages=new ArrayList<>();

        String systemmessage="You are a helpful customer support assistant. Provide clear and concise answers to customer inquiries.";

        messages.add((Message) new SystemMessage(systemmessage));
        messages.add((Message) new UserMessage(message));

        ChatClient.CallResponseSpec responseSpec= chatClient.prompt()
                .messages((org.springframework.ai.chat.messages.Message) messages)
                .call();



        return responseSpec.content();
    }
}
