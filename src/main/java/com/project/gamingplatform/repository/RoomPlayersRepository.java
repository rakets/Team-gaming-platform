package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.RoomPlayers;
import com.project.gamingplatform.entity.RoomPlayersId;
import com.project.gamingplatform.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPlayersRepository extends JpaRepository<RoomPlayers, RoomPlayersId> {
    boolean existsByRoomAndUser(GameRooms roomId, Users userId);
}
