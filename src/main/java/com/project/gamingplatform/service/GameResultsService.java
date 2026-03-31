package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameResultsResponse;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.SessionGameStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameResultsService {
    private final UsersService usersService;

    public GameResultsService(UsersService usersService) {
        this.usersService = usersService;
    }

    public GameResultsResponse getGameResult(int roomId){
        List<UsersDTO> winners = usersService.getWinners(roomId);
        GameResultsResponse resultsResponse = new GameResultsResponse(winners);
        if ( winners != null ) {
            resultsResponse.setGameStatus(SessionGameStatus.FINISHED);
            return resultsResponse;
        }
        return resultsResponse;
    }
}
