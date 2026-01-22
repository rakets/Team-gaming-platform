package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameRooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoomsRepository extends JpaRepository<GameRooms, Integer> {
}
