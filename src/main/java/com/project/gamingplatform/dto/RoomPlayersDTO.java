package com.project.gamingplatform.dto;

import com.project.gamingplatform.entity.RoleInRoom;
import lombok.Data;

@Data
public class RoomPlayersDTO {
    private Integer roomId;
    private Integer userId;

    private String username;

    private Boolean isReady;
    private RoleInRoom roleInRoom;

    public RoomPlayersDTO() {
    }
}