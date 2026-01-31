package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.RoomPlayers;
import com.project.gamingplatform.entity.RoomPlayersId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomPlayersRepository extends JpaRepository<RoomPlayers, RoomPlayersId> {
}
