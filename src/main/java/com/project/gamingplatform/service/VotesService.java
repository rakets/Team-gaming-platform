package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.*;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.RoomPlayers;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.entity.Votes;
import com.project.gamingplatform.repository.GameSessionsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.repository.VotesRepository;
import com.project.gamingplatform.websocket.WebSocketService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotesService {
    private final GameSessionsRepository gameSessionsRepository;
    private final UsersRepository usersRepository;
    private final VotesRepository votesRepository;
    private final RoomPlayersRepository roomPlayersRepository;
    private final WebSocketService webSocketService;
    private final UsersService usersService;

    public VotesService(GameSessionsRepository gameSessionsRepository,
                        UsersRepository usersRepository,
                        VotesRepository votesRepository,
                        RoomPlayersRepository roomPlayersRepository,
                        WebSocketService webSocketService,
                        UsersService usersService) {
        this.gameSessionsRepository = gameSessionsRepository;
        this.usersRepository = usersRepository;
        this.votesRepository = votesRepository;
        this.roomPlayersRepository = roomPlayersRepository;
        this.webSocketService = webSocketService;
        this.usersService = usersService;
    }

    // checking if there is a voice in the round and saving a new voice
    @Transactional
    public boolean newVote(VotesDTO voteDTO) {
        GameSessions gameSession = gameSessionsRepository.getGameSessionsByRoomId(voteDTO.getRoomId());
        if (votesRepository.existsByUserVoter_UserIdAndRoundNumAndSession_SessionId(voteDTO.getUserId(), gameSession.getCurrentRound(), gameSession.getSessionId())) {
            return false;
        } else {
            Users userVoter = usersRepository.findById(voteDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found with id: " + voteDTO.getUserId()));
            Users userTarget = usersRepository.findById(voteDTO.getVote()).orElseThrow(() -> new RuntimeException("User not found with id: " + voteDTO.getVote()));
            Votes vote = new Votes(gameSession.getCurrentRound(), gameSession, userVoter, userTarget);
            votesRepository.save(vote);
            return true;
        }
    }

    // receiving determining and returning data of the eliminated player
    @Transactional
    public UsersDTO getDeadPlayer(int roomId) {
        GameSessions gameSession = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        List<VoteResult> voteResultList = votesRepository.getTargetUserAndSumVotes(gameSession.getSessionId(), gameSession.getCurrentRound());
        // we get a list of live voters (to check later if everyone voted)
        List<RoomPlayers> playersInRoom = roomPlayersRepository.findAliveRoomPlayersByRoomId(roomId);
        // we get a list of all the votes in the round
        List<Votes> allVotesInRound = votesRepository.findAllVotesBySessionAndRoundNum(gameSession, gameSession.getCurrentRound());
        int userId = 0;
        Long voteCount = Long.valueOf(0);
        // if the list sizes are equal, it means that all the players have voted.
        // it is determined who has the most votes
        if (playersInRoom.size() == allVotesInRound.size()) {
            for (VoteResult result : voteResultList) {
                if (result.getCount() > voteCount) { // in case there are several users with different votes
                    userId = result.getTargetId();
                    voteCount = result.getCount();
                } else if (result.getCount().equals(voteCount)) { // in case there are several players with the same number of votes
                    userId = 0;
                }
            }
        }
        UsersDTO user = new UsersDTO();
        // if the vote did not pass (the selected player's ID is 0), then we clean the voting history.
        // if the vote has passed, then we give the player the status of 'DEAD'.
        if (userId == 0) {
            user.setUserId(userId);
            votesRepository.deleteAllBySessionAndRoundNum(gameSession, gameSession.getCurrentRound());
        } else {
            roomPlayersRepository.updateDeadStatusByRoomIdUserId(roomId, userId, true);
            user = usersService.findById(userId);
            user.setDeadStatus(DeadStatus.DEAD);
        }
        webSocketService.sendVotingResult(user, roomId);
        return user;
    }
}