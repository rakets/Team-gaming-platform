package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameResultsResponse;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.*;
import com.project.gamingplatform.repository.GameResultsRepository;
import com.project.gamingplatform.repository.GameSessionsRepository;
import com.project.gamingplatform.repository.RolesRepository;
import com.project.gamingplatform.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;

@Service
public class GameResultsService {
    private final UsersService usersService;
    private final GameSessionsRepository gameSessionsRepository;
    private final UsersRepository usersRepository;
    private final GameResultsRepository gameResultsRepository;
    private final RolesRepository rolesRepository;

    public GameResultsService(UsersService usersService,
                              GameSessionsRepository gameSessionsRepository,
                              UsersRepository usersRepository,
                              GameResultsRepository gameResultsRepository, RolesRepository rolesRepository) {
        this.usersService = usersService;
        this.gameSessionsRepository = gameSessionsRepository;
        this.usersRepository = usersRepository;
        this.gameResultsRepository = gameResultsRepository;
        this.rolesRepository = rolesRepository;
    }

    @Transactional
    public GameResultsResponse getGameResult(int roomId){
        List<UsersDTO> winners = usersService.getWinners(roomId);
        GameResultsResponse resultsResponse = new GameResultsResponse(winners);
        if ( winners != null ) {
            resultsResponse.setGameStatus(SessionGameStatus.FINISHED);
            saveGameResult(roomId);
            return resultsResponse;
        }
        return resultsResponse;
    }

    @Transactional
    //сохранение 2 игроков в таблицу game_results
    public void saveGameResult(int roomId) {
        List<Users> winners = usersRepository.findAlivePLayersByRoomId(roomId);
        GameSessions gameSession = gameSessionsRepository.getGameSessionsByRoomId(roomId);
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
