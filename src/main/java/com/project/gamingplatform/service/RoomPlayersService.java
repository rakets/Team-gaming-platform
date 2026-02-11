package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.dto.ReadyStatus;
import com.project.gamingplatform.dto.RoomPlayersDTO;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.*;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.util.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomPlayersService {
    private final RoomPlayersRepository roomPlayersRepository;
    private final GameRoomsRepository gameRoomsRepository;

    @Autowired
    public RoomPlayersService(RoomPlayersRepository roomPlayersRepository,
                              GameRoomsRepository gameRoomsRepository) {
        this.roomPlayersRepository = roomPlayersRepository;
        this.gameRoomsRepository = gameRoomsRepository;
    }

    // save RoomPlayer and adding as MODERATOR
    @Transactional
    public void saveRoomPlayers(GameRooms savedGameRoom){
        //take current user from context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();

        //save new RoomPlayer as MODERATOR
        RoomPlayers roomPlayers = new RoomPlayers(savedGameRoom, user, RoleInRoom.MODERATOR);
        roomPlayersRepository.save(roomPlayers);
    }

    // join to RoomPlayer as PLAYER
    @Transactional
    public void joinToRoomPlayer(GameRooms gameRoom){
        //take current user from context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();
        if(roomPlayersRepository.existsByRoomAndUser(gameRoom, user)){
            return;
        }


        //save new RoomPlayer as PLAYER
        RoomPlayers roomPlayers = new RoomPlayers(gameRoom, user, RoleInRoom.PLAYER);
        roomPlayersRepository.save(roomPlayers);
    }

    @Transactional
    public void cleanRoomPlayers(int roomId, int userId){
        roomPlayersRepository.deleteAllPlayersExceptModerator(roomId);
        roomPlayersRepository.updateUserAsUnready(userId, roomId);
    }
    // ОТ GameRoomsService -> бд
    @Transactional
    public void userIsReady(int userId, int roomId){
        // сохранение ready в бд
        roomPlayersRepository.updateUserAsReady(userId, roomId);
    }

    @Transactional
    public void userIsUnready(int userId, int roomId){
        // сохранение ready в бд
        roomPlayersRepository.updateUserAsUnready(userId, roomId);
    }

    public List<RoomPlayers> findAllRoomPlayersByRoomId(int roomId){
        List<RoomPlayers> roomPlayersList = roomPlayersRepository.findAllRoomPlayersByRoomId(roomId);
        return roomPlayersList;
    }

    public boolean isUserReadyInRoom(int roomId, int userId){
        return roomPlayersRepository.isUserReadyInRoom(roomId, userId);
    }
}
