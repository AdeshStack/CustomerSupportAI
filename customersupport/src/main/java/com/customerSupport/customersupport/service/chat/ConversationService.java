package com.customerSupport.customersupport.service.chat;

import com.customerSupport.customersupport.dtos.ChatEntry;
import com.customerSupport.customersupport.dtos.ChatMessageDto;
import com.customerSupport.customersupport.service.ticket.AISupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationService implements IConversationService {

    private final AISupportService aiSupportService;
    private final Map<String, List<ChatEntry>> activeConversations = new ConcurrentHashMap<>();

    @Override
    public String handleChatMessage(ChatMessageDto chatMessage) {
        String sessionId = chatMessage.getSessionId();
        String userMessage = chatMessage.getMessage() != null ? chatMessage.getMessage().trim() : "";

        List<ChatEntry> history = activeConversations.computeIfAbsent(sessionId,
                k -> Collections.synchronizedList(new ArrayList<>()));


//        String correctedCustomerInformation = getCorrectedCustomerInformation(sessionId, userMessage, history);
//        log.info("********************* The corrected customer information: {}", correctedCustomerInformation);
//        if (correctedCustomerInformation != null) {
//            return correctedCustomerInformation;
//        }


        history.add(new ChatEntry("user", userMessage));

        String aiResponseText;
        try {
            aiResponseText = aiSupportService.chatWithHistory(history).block();
        } catch (Exception e) {
            aiResponseText = "Sorry, I'm having trouble processing response right now.";
        }

        if (aiResponseText == null || aiResponseText.isEmpty()) {
            return "";
        }

        history.add(new ChatEntry("assistant", aiResponseText));

//        if (aiResponseText.contains("TICKET_CREATION_READY")) {
//            try {
//                String confirmationMessage = aiSupportService.generateUserConfirmationMessage().block();
//                history.add(new ChatEntry("assistant", confirmationMessage));
//
//                finalizeConversationAndNotify(sessionId, history);
//                return confirmationMessage;
//            } catch (Exception e) {
//                return "Error generating confirmation message.";
//            }
//        }

        return aiResponseText;
    }
}
