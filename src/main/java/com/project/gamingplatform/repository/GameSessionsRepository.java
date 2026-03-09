package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSessionsRepository extends JpaRepository<GameSessions, Integer> {
    boolean existsByGameRoomsRoomIdAndStatus(Integer roomId, SessionGameStatus status);

    @Query("SELECT g FROM GameSessions g WHERE g.gameRooms.roomId = :roomId")
    GameSessions getGameSessionsByRoomId(@Param("roomId") Integer roomId);

    @Modifying
    @Transactional
    @Query("UPDATE GameSessions g SET g.status = :newStatus WHERE g.sessionId = :sessionId")
    void updateGameSessionStatus(@Param("newStatus")SessionGameStatus status,
                                 @Param("sessionId")Integer sessionId);
}
