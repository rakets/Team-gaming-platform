package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.VotesDTO;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.entity.Votes;
import com.project.gamingplatform.repository.GameSessionsRepository;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.repository.VotesRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class VotesService {
    private final GameSessionsRepository gameSessionsRepository;
    private final UsersRepository usersRepository;
    private final VotesRepository votesRepository;

    public VotesService(GameSessionsRepository gameSessionsRepository, UsersRepository usersRepository, VotesRepository votesRepository) {
        this.gameSessionsRepository = gameSessionsRepository;
        this.usersRepository = usersRepository;
        this.votesRepository = votesRepository;
    }

    // проверка есть ли голос в раунде и сохранение нового голоса
    @Transactional
    public boolean newVote(VotesDTO voteDTO) {
        GameSessions gameSession = gameSessionsRepository.getGameSessionsByRoomId(voteDTO.getRoomId());
        if (votesRepository.existsByUserVoter_UserIdAndRoundNum(voteDTO.getUserId(), gameSession.getCurrentRound())) {
            return false;
        } else {
            if (voteDTO.getVote().equals(0)) {
                return true;
            }
            Users userVoter = usersRepository.findById(voteDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found with id: " + voteDTO.getUserId()));
            Users userTarget = usersRepository.findById(voteDTO.getVote()).orElseThrow(() -> new RuntimeException("User not found with id: " + voteDTO.getVote()));
            Votes vote = new Votes(gameSession.getCurrentRound(), gameSession, userVoter, userTarget);
            votesRepository.save(vote);
            return true;
        }
    }
}
