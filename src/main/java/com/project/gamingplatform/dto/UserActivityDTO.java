package com.project.gamingplatform.dto;

import lombok.Data;

@Data
public class UserActivityDTO {
    private String username;
    private MessageType type; // JOIN / LEAVE

    public UserActivityDTO(String username, MessageType type) {
        this.username = username;
        this.type = type;
    }
}
