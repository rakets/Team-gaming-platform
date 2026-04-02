package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.dto.MessageType;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.GameSessionsRepository;
import com.project.gamingplatform.websocket.WebSocketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameSessionsService {
    private final GameSessionsRepository gameSessionsRepository;
    private final GameProcessService gameProcessService;
    private final WebSocketService webSocketService;

    public GameSessionsService(GameSessionsRepository gameSessionsRepository,
                               GameProcessService gameProcessService,
                               WebSocketService webSocketService) {
        this.gameSessionsRepository = gameSessionsRepository;
        this.gameProcessService = gameProcessService;
        this.webSocketService = webSocketService;
    }

    //bunker cards distributing
    public void settingUpTheGameSession(Integer roomId){
        GameSessions gameSessions = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        gameProcessService.distributionGameCards(gameSessions);
        updateGameSessionStatus(SessionGameStatus.IN_PROGRESS, roomId);
    }

    //updating status of game session
    @Transactional
    public void updateGameSessionStatus(SessionGameStatus status, Integer roomId){
        GameSessions gameSessions = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        if (gameSessions != null) {
            gameSessionsRepository.updateGameSessionStatus(status, gameSessions.getSessionId());
        } else {
            throw new EntityNotFoundException("Session for room " + roomId + " not found");
        }
    }

//  метод проверки и задания следующего раунда
    @Transactional
    public boolean nextRound(int round, int roomId){
        GameSessions gameSession = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        if(gameSession.getCurrentRound() == round) {
            round++;
            gameSessionsRepository.updateGameSessionsCurrentRound(round, gameSession.getSessionId());
            webSocketService.updateRound(round, roomId);
            return true;
        }
        return false;
    }
}