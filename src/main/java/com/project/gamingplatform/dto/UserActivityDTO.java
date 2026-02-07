package com.project.gamingplatform.dto;

import com.project.gamingplatform.entity.RoleInRoom;
import lombok.Data;

@Data
public class UserActivityDTO {
    private String username;
    private MessageType messageType; // JOIN / LEAVE
    private RoleInRoom roleInRoom; // MODERATOR / PLAYER

    public UserActivityDTO() {
    }
}
