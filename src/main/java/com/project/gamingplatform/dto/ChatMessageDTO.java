package com.project.gamingplatform.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private Integer roomId;
    private Integer senderId;
    private String senderName;
    private String content;
    private String timestamp;
}
