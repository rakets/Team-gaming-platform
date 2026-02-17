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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE RoomPlayers r SET r.isReady = true WHERE r.id.userId = :userId AND r.id.roomId = :roomId")
    void updateUserAsReady(@Param("userId") int userId, @Param("roomId") int roomId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE RoomPlayers r SET r.isReady = false WHERE r.id.userId = :userId AND r.id.roomId = :roomId")
    void updateUserAsUnready(@Param("userId") int userId, @Param("roomId") int roomId);

    @Query("SELECT r FROM RoomPlayers r WHERE r.id.roomId = :roomId")
    List<RoomPlayers> findAllRoomPlayersByRoomId(@Param("roomId") int roomId);

    @Query("SELECT r.isReady FROM RoomPlayers r WHERE r.id.userId = :userId AND r.id.roomId = :roomId")
    boolean isUserReadyInRoom(@Param("roomId") int roomId, @Param("userId") int userId);

    @Query("SELECT COUNT(r) = 0 FROM RoomPlayers r WHERE r.id.roomId = :roomId AND r.isReady <> true")
    boolean isAllUserReadyInRoom(@Param("roomId") int roomId);
}
