package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);

    @Query("SELECT r.user FROM RoomPlayers r WHERE r.room.roomId = :idGameRoom")
    List<Users> findAllUsersByRoomId(@Param("idGameRoom")int idRoom);
}

