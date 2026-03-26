package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.MessageType;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.dto.VoteResult;
import com.project.gamingplatform.dto.VotesDTO;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.RoomPlayers;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.entity.Votes;
import com.project.gamingplatform.repository.GameSessionsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.repository.VotesRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotesService {
    private final GameSessionsRepository gameSessionsRepository;
    private final UsersRepository usersRepository;
    private final VotesRepository votesRepository;
    private final RoomPlayersRepository roomPlayersRepository;

    public VotesService(GameSessionsRepository gameSessionsRepository, UsersRepository usersRepository, VotesRepository votesRepository, RoomPlayersRepository roomPlayersRepository) {
        this.gameSessionsRepository = gameSessionsRepository;
        this.usersRepository = usersRepository;
        this.votesRepository = votesRepository;
        this.roomPlayersRepository = roomPlayersRepository;
    }

    // проверка есть ли голос в раунде и сохранение нового голоса
    @Transactional
    public boolean newVote(VotesDTO voteDTO) {
        GameSessions gameSession = gameSessionsRepository.getGameSessionsByRoomId(voteDTO.getRoomId());
        if (votesRepository.existsByUserVoter_UserIdAndRoundNum(voteDTO.getUserId(), gameSession.getCurrentRound())) {
            return false;
        } else {
//            if (voteDTO.getVote().equals(0)) {
//                return true;
//            }
            Users userVoter = usersRepository.findById(voteDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found with id: " + voteDTO.getUserId()));
            Users userTarget = usersRepository.findById(voteDTO.getVote()).orElseThrow(() -> new RuntimeException("User not found with id: " + voteDTO.getVote()));
            Votes vote = new Votes(gameSession.getCurrentRound(), gameSession, userVoter, userTarget);
            votesRepository.save(vote);
            return true;
        }
    }

    //получение определение и возврат данных выбывшего игрока
    @Transactional
    public UsersDTO getDeadPlayer(int roomId) {
        GameSessions gameSession = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        List<VoteResult> voteResultList = votesRepository.getTargetUserAndSumVotes(gameSession.getSessionId(), gameSession.getCurrentRound());
//        System.out.println("GOLOSOVANIE: ");
//        System.out.println(voteResultList);

        List<RoomPlayers> playersInRoom = roomPlayersRepository.findAllRoomPlayersByRoomId(roomId);
        List<Votes> allVotesInRound = votesRepository.findAllVotesBySessionAndRoundNum(gameSession, gameSession.getCurrentRound());
//        System.out.println("All Votes, size: " + allVotesInRound.size());
//        System.out.println("allVotesInRound");
        int userId = 0;
        Long voteCount = Long.valueOf(0);

        if (playersInRoom.size() == allVotesInRound.size()) {
            for (VoteResult result : voteResultList) {
                if (result.getCount() > voteCount) { //случай, если несколько пользователей, с разным кол-вом голосов
                    userId = result.getTargetId();
                    voteCount = result.getCount();
                } else if (result.getCount().equals(voteCount)) { //случай, если несколько игроков с одинаковым кол-вом голосов
                    userId = 0;
                }
            }
        }

//        System.out.println("UserID: " + userId + " count:  " + voteCount);
        UsersDTO user = new UsersDTO();
        user.setUserId(userId);
        user.setMessageType(MessageType.VOTING_RESULT);
//        System.out.println("User ID: " + user.getUserId());
        //если голосование не прошло (ID выбранного игрока 0), то чистим историю голосов
        //если голосование прошло, то даем игроку статус 'DEAD'
        if (userId == 0) {
            votesRepository.deleteAllBySessionAndRoundNum(gameSession, gameSession.getCurrentRound());
        } else {
            roomPlayersRepository.updateDeadStatusByRoomIdUserId(roomId, userId);
        }
        return user;
    }
}