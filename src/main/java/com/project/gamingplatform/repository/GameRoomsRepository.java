package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRoomsRepository extends JpaRepository<GameRooms, Integer> {

    List<GameRooms> findAllByCreatedBy(Users userId);
    Optional<GameRooms> findByRoomName(String roomName);
}
