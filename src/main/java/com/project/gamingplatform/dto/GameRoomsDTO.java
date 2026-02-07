package com.project.gamingplatform.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class GameRoomsDTO {
    private Integer roomId;

    @NotBlank(message = "Name of room can't be empty.")
    private String roomName;

    @Max(value = 10, message = "10 players it's maximum")
    @Min(value = 4, message = "4 players it's minimum")
    private int numPlayers;

    private Integer createdBy;
    private boolean isCurrentUserModerator;

    private List<UsersDTO> usersInRoom; //list of users in this room
}
