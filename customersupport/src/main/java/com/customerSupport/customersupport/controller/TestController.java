package com.customerSupport.customersupport.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;


import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController

@RequestMapping("/chat")
@RequiredArgsConstructor // it generates a constructor that takes a ChatClient parameter, enabling dependency injection.
public class TestController {

    private final ChatClient chatClient;

    @GetMapping("/chat-with-text")
    public String chatWithAi(@RequestParam String message){

        List<Message>  messages=new ArrayList<>();

        String systemmessage="You are a helpful customer support assistant. Provide clear and concise answers to customer inquiries.";

        messages.add(new SystemMessage(systemmessage)); // for specifying the role of the AI
        messages.add(new UserMessage(message));// Adding user message to the list

        ChatClient.CallResponseSpec responseSpec= chatClient.prompt()
                .messages(messages).options(ChatOptions.builder() // you can customize the ai model parameters here
                        .model("gpt-5-nano")
                        .temperature(1.0)
                        .build())
                .call();

        return responseSpec.content();
    }




    @PostMapping(
            value = "/describe-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String describeImage(
            @RequestPart("image") MultipartFile image
    ) throws IOException {

        Resource imageResource = new ByteArrayResource(image.getBytes()) {   //This creates an in-memory Resource that contains the uploaded file bytes and overrides getFilename() to return the original client filename.
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };

        return chatClient.prompt()
                .system("""
                You are a vision assistant.
                Describe what is happening in the image clearly and accurately.
                """)
                .user(user -> user
                        .text("Describe this image in detail.")
                        .media(MimeType.valueOf(image.getContentType()), imageResource) // image as media and byte ,file information.
                )
                .options(ChatOptions.builder()
                        .model("gpt-5-nano")   // Vision-capable model
                        .temperature(1.0)
                        .build()
                )
                .call()
                .content();
    }


    @PostMapping(
            value = "/audio-to-text",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String audioToText(
            @RequestPart("audio") MultipartFile audio
    ) throws IOException {

        Resource audioResource = new ByteArrayResource(audio.getBytes()) {
            @Override
            public String getFilename() {
                return audio.getOriginalFilename();
            }
        };

        return chatClient.prompt()
                .system("""
                You are a speech recognition assistant.
                Convert the spoken audio accurately into text.
                """)
                .user(user -> user
                        .text("Transcribe this audio to text.")
                        .media(MimeType.valueOf(audio.getContentType()), audioResource)
                )
                .options(ChatOptions.builder()
                        .model("gpt-5-nano")   // supports audio input
                        .temperature(0.0)
                        .build()
                )
                .call()
                .content();
    }


}
