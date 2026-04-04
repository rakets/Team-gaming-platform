package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.dto.MessageType;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.GameSessionsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.util.CustomUserDetails;
import com.project.gamingplatform.websocket.WebSocketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GameSessionsService {
    private final GameSessionsRepository gameSessionsRepository;
    private final GameProcessService gameProcessService;
    private final WebSocketService webSocketService;
    private final RoomPlayersRepository roomPlayersRepository;
    private final GameRoomsRepository gameRoomsRepository;

    public GameSessionsService(GameSessionsRepository gameSessionsRepository,
                               GameProcessService gameProcessService,
                               WebSocketService webSocketService,
                               RoomPlayersRepository roomPlayersRepository,
                               GameRoomsRepository gameRoomsRepository) {
        this.gameSessionsRepository = gameSessionsRepository;
        this.gameProcessService = gameProcessService;
        this.webSocketService = webSocketService;
        this.roomPlayersRepository = roomPlayersRepository;
        this.gameRoomsRepository = gameRoomsRepository;
    }

    //bunker cards distributing
    public void settingUpTheGameSession(Integer roomId) {
        GameSessions gameSessions = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        gameProcessService.distributionGameCards(gameSessions);
        updateGameSessionStatus(SessionGameStatus.IN_PROGRESS, roomId);
    }

    //updating status of game session
    @Transactional
    public void updateGameSessionStatus(SessionGameStatus status, Integer roomId) {
        GameSessions gameSessions = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        if (gameSessions != null) {
            gameSessionsRepository.updateGameSessionStatus(status, gameSessions.getSessionId());
        } else {
            throw new EntityNotFoundException("Session for room " + roomId + " not found");
        }
    }

    //  метод проверки и задания следующего раунда
    @Transactional
    public boolean nextRound(int round, int roomId) {
        GameSessions gameSession = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        if (gameSession.getCurrentRound() == round) {
            round++;
            gameSessionsRepository.updateGameSessionsCurrentRound(round, gameSession.getSessionId());
            webSocketService.updateRound(round, roomId);
            return true;
        }
        return false;
    }

    // метод проверки, является ли статус сессии IN_PROGRESS
    @Transactional
    public Map<String, Object> isGameSessionInProgressAndPlayerInList(int roomId, int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users currentUser = userDetails.getUser();
        GameRooms gameRoom = gameRoomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("The room was not found when the player tried to join the room."));
        // получение статуса сессии
        SessionGameStatus status = gameSessionsRepository.getGameSessionsStatusByGameRoomId(roomId);
        Map<String, Object> map = new HashMap<>();
        map.put("sessionStatus", status);
        // имеет ли сессия статус IN_PROGRESS и находится ли игрок в списке игроков комнаты
        map.put("isUserCanJOIN", status.equals(SessionGameStatus.IN_PROGRESS) && roomPlayersRepository.existsByRoomAndUser(gameRoom, currentUser));
        return map;
    }
}