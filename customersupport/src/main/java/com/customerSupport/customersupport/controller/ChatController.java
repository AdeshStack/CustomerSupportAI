package com.customerSupport.customersupport.controller;


import com.customerSupport.customersupport.dtos.ChatMessageDto;
import com.customerSupport.customersupport.service.chat.IConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/chat")
@RequiredArgsConstructor
public class ChatController {

//    private final ChatClient chatClient;

    private final IConversationService conversationService;

//    @GetMapping
//    public String chat(@RequestParam String message){
//
//
//        List<Message> messages=new ArrayList<>();
//        String systemPrompt="You are a helpful agent. Your goal is the listen to the user question and" +
//                " provide a response";
//
//        messages.add(new SystemMessage(systemPrompt));
//        messages.add(new UserMessage(message));
//
//        ChatClient.CallResponseSpec responseSpec= chatClient.prompt()
//                .messages(messages).options(ChatOptions.builder() // you can customize the ai model parameters here
//                        .model("gpt-5-nano")
//                        .temperature(1.0)
//                        .build())
//                .call();
//
//        return responseSpec.content();
//
//    }

    @PostMapping
    public ResponseEntity<String> handleChatMessage(@RequestBody ChatMessageDto message){

        String response = conversationService.handleChatMessage(message);

        return ResponseEntity.ok( response);
    }
}
