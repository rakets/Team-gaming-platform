package com.project.gamingplatform.repository;

import com.project.gamingplatform.dto.PlayerCardsDTO;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.PlayerCards;
import com.project.gamingplatform.entity.PlayerCardsId;
import com.project.gamingplatform.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerCardsRepository extends JpaRepository<PlayerCards, PlayerCardsId> {
    @Modifying
    @Transactional
    void deleteAllBySession(GameSessions gameSessions);

    //обновление статуса карты !!! исправить, когда уберу session_id в табл game_session
    @Transactional
    @Modifying
    @Query("UPDATE PlayerCards pc SET pc.revealed = true WHERE pc.user.userId = :userId AND pc.bunkerCards.cardId = :cardId")
    void updateRevealed(@Param("userId") Integer userId,
                        @Param("cardId") Integer cardId);
}
