package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.GameSessionsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameSessionsService {
    private final GameSessionsRepository gameSessionsRepository;
    private final GameProcessService gameProcessService;

    public GameSessionsService(GameSessionsRepository gameSessionsRepository,
                               GameProcessService gameProcessService) {
        this.gameSessionsRepository = gameSessionsRepository;
        this.gameProcessService = gameProcessService;
    }

    public void settingUpTheGameSession(Integer roomId){
        GameSessions gameSessions = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        gameProcessService.distributionGameCards(gameSessions);
    }
}