package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.GameSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSessionsRepository extends JpaRepository<GameSessions, Integer> {
    boolean existsByGameRooms(GameRooms roomId);

    @Query("SELECT g FROM GameSessions g WHERE g.gameRooms.roomId = :roomId")
    GameSessions getGameSessionsByRoomId(@Param("roomId") Integer roomId);
}
