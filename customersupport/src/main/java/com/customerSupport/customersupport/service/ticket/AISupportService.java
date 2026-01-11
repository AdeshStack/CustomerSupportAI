package com.customerSupport.customersupport.service.ticket;

import com.customerSupport.customersupport.dtos.ChatEntry;
import com.customerSupport.customersupport.util.PromptTemplates;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.scheduling.quartz.SchedulerAccessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Service
public class AISupportService {
    private final ChatClient chatClient;

    // from reactive to send 0 or 1
    // for this method ai will looking back the history before reply
    //This method converts chat history into LLM messages, calls the AI in a blocking-safe manner using boundedElastic, and returns the response reactively as a Mono.
    public Mono<String> chatWithHistory(List<ChatEntry> history){
    List<Message> messages=new ArrayList<>();

    messages.add(new SystemMessage(PromptTemplates.SUPPORT_PROMPT_TEMPLATE));

    for(ChatEntry chatEntry: history){ // Iterates over previous chats stored in DB or memory.
        String  role=chatEntry.getRole();
        String content=chatEntry.getContent();
        switch (role){
            case "user": messages.add(new UserMessage(content));break;
            case "assistance": messages.add(new AssistantMessage(content)); break;
            default:{
                //Optional log  anything here

            }
        }
    }

    return Mono.fromCallable(()->{
        ChatClient.CallResponseSpec responseSpec =  chatClient.prompt().messages(messages).call();

        String  content = responseSpec.content();
        if(content==null){
            throw new IllegalStateException("No content  available");
        }
        return content;

    }).subscribeOn(Schedulers.boundedElastic());


    }

}
