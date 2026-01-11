package com.customerSupport.customersupport.service.chat;

import com.customerSupport.customersupport.dtos.ChatMessageDto;

public interface IConversationService {

    String handleChatMessage(ChatMessageDto message);
}
