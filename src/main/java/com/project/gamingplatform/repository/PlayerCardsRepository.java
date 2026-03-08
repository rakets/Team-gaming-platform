package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.PlayerCards;
import com.project.gamingplatform.entity.PlayerCardsId;
import com.project.gamingplatform.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface PlayerCardsRepository extends JpaRepository<PlayerCards, PlayerCardsId> {
    @Modifying
    @Transactional
    void deleteAllBySession(GameSessions gameSessions);
}
