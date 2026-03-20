package com.project.gamingplatform.dto;

import com.project.gamingplatform.entity.GlobalRole;
import com.project.gamingplatform.entity.RoleInRoom;
import lombok.Data;

@Data
public class UsersDTO {
    private Integer userId;
    private String username;
    private GlobalRole globalRole;
    private RoleInRoom gameRole = RoleInRoom.PLAYER;

//    для кнопки Ready
    private ReadyStatus readyStatus = ReadyStatus.UNREADY;

    private MessageType messageType; // JOIN / LEAVE / READY / UNREADY / JOIN_GAME_SESSION

    // bunker cards
    private BunkerCardList bunkerCards;

    // game session info
    private GameSessionInfo gameSessionInfo;
}
