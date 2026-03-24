package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.entity.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotesRepository extends JpaRepository<Votes, Integer> {
    // проверка голосовал ли игрок в этом раунде за кого-то
    boolean existsByUserVoter_UserIdAndRoundNum(Integer voterID, Integer roundNum);
}
