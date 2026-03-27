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

        private DeadStatus deadStatus = DeadStatus.ALIVE;

        // JOIN / LEAVE / READY / UNREADY / JOIN_GAME_SESSION / VOTING_RESULT / NEXT_ROUND
        private MessageType messageType;

        // bunker cards
        private BunkerCardList revealedBunkerCards;
        private BunkerCardList unrevealedBunkerCards;

        // game session info
        private GameSessionInfo gameSessionInfo;
        //значение нового раунда, для передаче все игрокам по WebSocket
        private int nextRound;
}
