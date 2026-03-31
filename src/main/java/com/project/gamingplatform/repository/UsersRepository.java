package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);

    // метод возвращает список всех игроков комнаты
    @Query("SELECT r.user FROM RoomPlayers r WHERE r.room.roomId = :idGameRoom")
    List<Users> findAllUsersByRoomId(@Param("idGameRoom")int idRoom);

    // метод возвращает список живых игроков комнаты
    @Query("SELECT r.user FROM RoomPlayers r WHERE r.room.roomId = :roomId AND r.isDead = false")
    List<Users> findAlivePLayersByRoomId(@Param("roomId") int roomID);
}

