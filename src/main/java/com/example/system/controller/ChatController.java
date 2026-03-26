package com.example.system.controller;

import com.example.system.dto.ChatRequest;
import com.example.system.service.HuggingFaceChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private HuggingFaceChatService chatService;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody ChatRequest request) {
        var messages = request.getMessages() != null ? request.getMessages() : Collections.<HuggingFaceChatService.ChatMessage>emptyList();
        String reply = chatService.chat(messages);
        return ResponseEntity.ok(Map.of("reply", reply));
    }
}
