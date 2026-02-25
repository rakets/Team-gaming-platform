package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.GameSessions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameSessionsRepository extends JpaRepository<GameSessions, Integer> {
    boolean existsByGameRooms(GameRooms roomId);
}
