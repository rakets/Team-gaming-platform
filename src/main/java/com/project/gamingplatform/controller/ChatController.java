package com.project.gamingplatform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.gamingplatform.dto.ChatMessageDTO;
import com.project.gamingplatform.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate messagingTemplate,
                          ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    public void processMessageFromClient(ChatMessageDTO message) throws JsonProcessingException {
        chatService.saveChatMessage(message);
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId() + "/chat", message);
    }

}