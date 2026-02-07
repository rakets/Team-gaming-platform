package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.RoomPlayers;
import com.project.gamingplatform.entity.RoomPlayersId;
import com.project.gamingplatform.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomPlayersRepository extends JpaRepository<RoomPlayers, RoomPlayersId> {
    boolean existsByRoomAndUser(GameRooms roomId, Users userId);

    @Modifying
    @Query("DELETE FROM RoomPlayers r WHERE r.room.roomId = :idGameRoom AND r.roleInRoom = 'PLAYER'")
    void deleteAllPlayersExceptModerator(@Param("idGameRoom") int idGameRoom);
}
