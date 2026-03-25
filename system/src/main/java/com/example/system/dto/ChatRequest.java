package com.example.system.dto;

import com.example.system.service.HuggingFaceChatService;

import java.util.List;

public class ChatRequest {
    private List<HuggingFaceChatService.ChatMessage> messages;

    public List<HuggingFaceChatService.ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<HuggingFaceChatService.ChatMessage> messages) {
        this.messages = messages;
    }
}
