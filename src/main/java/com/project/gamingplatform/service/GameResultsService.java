package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.MessageType;
import com.project.gamingplatform.dto.ServerMessage;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.*;
import com.project.gamingplatform.repository.GameResultsRepository;
import com.project.gamingplatform.repository.GameSessionsRepository;
import com.project.gamingplatform.repository.RolesRepository;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.websocket.WebSocketService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameResultsService {
    private final UsersService usersService;
    private final GameSessionsRepository gameSessionsRepository;
    private final UsersRepository usersRepository;
    private final GameResultsRepository gameResultsRepository;
    private final RolesRepository rolesRepository;
    private final WebSocketService webSocketService;

    public GameResultsService(UsersService usersService,
                              GameSessionsRepository gameSessionsRepository,
                              UsersRepository usersRepository,
                              GameResultsRepository gameResultsRepository,
                              RolesRepository rolesRepository,
                              WebSocketService webSocketService) {
        this.usersService = usersService;
        this.gameSessionsRepository = gameSessionsRepository;
        this.usersRepository = usersRepository;
        this.gameResultsRepository = gameResultsRepository;
        this.rolesRepository = rolesRepository;
        this.webSocketService = webSocketService;
    }

    @Transactional
    public SessionGameStatus getGameResult(int roomId) {
        List<UsersDTO> winners = usersService.getWinners(roomId);
        ServerMessage serverMessage = new ServerMessage();
        // если список победителей не пуст, то вернет FINISH
        if (winners != null) {
            GameSessions gameSession = gameSessionsRepository.getGameSessionsByRoomId(roomId);
            // сохранение результатов игры
            saveGameResult(roomId, gameSession);
            // установление статуса сессии FINISHED
            gameSessionsRepository.updateGameSessionStatus(SessionGameStatus.FINISHED, gameSession.getSessionId());
            // сбор и передача данных на вебсокет
            serverMessage.setUsersList(winners);
            serverMessage.setMessageType(MessageType.GAME_RESULT);
            webSocketService.sendGameResult(serverMessage, roomId);
            return SessionGameStatus.FINISHED;
        }
        // случай, если список победителей не пуст
        return SessionGameStatus.IN_PROGRESS;
    }

    @Transactional
    //сохранение 2 игроков в таблицу game_results
    public void saveGameResult(int roomId, GameSessions gameSession) {
        List<Users> winners = usersRepository.findAlivePLayersByRoomId(roomId);
        Roles role = rolesRepository.findRolesByRoleName("Survivor");
        for (Users user : winners) {
            GameResults gameResult = new GameResults();
            gameResult.setGameSessions(gameSession);
            gameResult.setWinnerUserId(user);
            gameResult.setWinnerRoleId(role);
            gameResultsRepository.save(gameResult);
        }
    }
}
