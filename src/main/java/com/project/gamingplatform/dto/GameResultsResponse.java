package com.project.gamingplatform.dto;

import com.project.gamingplatform.entity.SessionGameStatus;
import lombok.Data;

import java.util.List;

@Data
public class GameResultsResponse {
    private SessionGameStatus gameStatus = SessionGameStatus.IN_PROGRESS;
    private List<UsersDTO> winners;
    private MessageType messageType = MessageType.GAME_RESULT;

    public GameResultsResponse(List<UsersDTO> winners) {
        this.winners = winners;
    }
}
