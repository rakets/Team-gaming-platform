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
    private final GameRoomsRepository gameRoomsRepository;

    public GameSessionsService(GameSessionsRepository gameSessionsRepository, GameRoomsRepository gameRoomsRepository) {
        this.gameSessionsRepository = gameSessionsRepository;
        this.gameRoomsRepository = gameRoomsRepository;
    }

    @Transactional
    public void createGameSession(int roomId){
        GameRooms gameRooms = gameRoomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("The room was not found, when user tried search room."));
        GameSessions gameSessions = new GameSessions(gameRooms);
        if (!gameSessionsRepository.existsByGameRooms(gameRooms)) {
            gameSessionsRepository.save(gameSessions);
            log.info("Game session for room ID: {} has been created.", roomId);
        }
    }
}