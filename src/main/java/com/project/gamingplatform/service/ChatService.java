package com.project.gamingplatform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gamingplatform.dto.ChatMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public ChatService(SimpMessagingTemplate messagingTemplate,
                          RedisTemplate<String, String> redisTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    public void saveChatMessage(ChatMessageDTO message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> messageMap = new HashMap<>();
        String key = "room:" + message.getRoomId();
        messageMap.put("senderId", message.getSenderId());
        messageMap.put("senderName", message.getSenderName());
        messageMap.put("content", message.getContent());
        messageMap.put("timestamp", message.getTimestamp());

        String jsonMessage = objectMapper.writeValueAsString(messageMap);
        redisTemplate.opsForList().rightPush(key, jsonMessage);
        redisTemplate.expire(key, 24, TimeUnit.MINUTES);
    }

    public List<ChatMessageDTO> getChatHistory(Integer roomId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String key = "room:" + roomId;
        List<String> messages = redisTemplate.opsForList().range(key, 0, -1);

        List<ChatMessageDTO> chatMessageDTOList = new ArrayList<>();
        for (String json : messages) {
            ChatMessageDTO chatMessageDTO = new ChatMessageDTO();

            Map<String, Object> msg = objectMapper.readValue(json, Map.class);
            chatMessageDTO.setRoomId(roomId);
            chatMessageDTO.setSenderId(Integer.valueOf(String.valueOf(msg.get("senderId"))));
            chatMessageDTO.setSenderName((String) msg.get("senderName"));
            chatMessageDTO.setContent((String) msg.get("content"));
            chatMessageDTO.setTimestamp(msg.get("timestamp").toString());

            chatMessageDTOList.add(chatMessageDTO);
        }
        return chatMessageDTOList;
    }
}
