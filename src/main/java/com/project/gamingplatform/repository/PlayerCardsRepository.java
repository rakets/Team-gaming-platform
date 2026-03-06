package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.PlayerCards;
import com.project.gamingplatform.entity.PlayerCardsId;
import com.project.gamingplatform.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerCardsRepository extends JpaRepository<PlayerCards, PlayerCardsId> {
    void deleteAllBySession(GameSessions gameSessions);
}
