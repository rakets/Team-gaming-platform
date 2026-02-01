package com.project.gamingplatform.service;

import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.repository.GameSessionsRepository;
import org.springframework.stereotype.Service;

@Service
public class GameSessionsService {
    private final GameSessionsRepository gameSessionsRepository;

    public GameSessionsService(GameSessionsRepository gameSessionsRepository) {
        this.gameSessionsRepository = gameSessionsRepository;
    }

    public void saveGameSession(GameRooms gameRooms){
        GameSessions gameSessions = new GameSessions(gameRooms);
        gameSessionsRepository.save(gameSessions);
    }
}
