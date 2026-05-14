package com.project.gamingplatform.repository;

import com.project.gamingplatform.dto.VoteResult;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.RoomPlayers;
import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.entity.Votes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotesRepository extends JpaRepository<Votes, Integer> {
    // "has player voted in round for someone" checking
//    boolean existsByUserVoter_UserIdAndRoundNum(Integer voterID, Integer roundNum);
    boolean existsByUserVoter_UserIdAndRoundNumAndSession_SessionId(Integer voterID, Integer roundNum, Integer sessionId);

    // method get list of VoteResult (player ID, vote count)
    @Query(value = "SELECT v.target_id AS targetId, COUNT(v.vote_id) AS count " +
            "FROM votes v " +
            "WHERE v.session_id = :sessionId AND v.round_num = :roundNum " +
            "GROUP BY v.target_id",
            nativeQuery = true)
    List<VoteResult> getTargetUserAndSumVotes(@Param("sessionId") int sessionId,
                                              @Param("roundNum") int round);

    // method get vote count of the session in current round
    List<Votes> findAllVotesBySessionAndRoundNum(GameSessions sessions, int round);

    // method for remowing votes in the round
    @Modifying
    @Transactional
    void deleteAllBySessionAndRoundNum(GameSessions gameSessions, int round);

    // method for remowing votes in the session
    @Modifying
    @Transactional
    void deleteAllBySession(GameSessions gameSessions);
}
