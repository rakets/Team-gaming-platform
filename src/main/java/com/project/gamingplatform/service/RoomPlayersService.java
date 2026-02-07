package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
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

    @Autowired
    public RoomPlayersService(RoomPlayersRepository roomPlayersRepository) {
        this.roomPlayersRepository = roomPlayersRepository;
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
    public void cleanRoomPlayers(int idGameRoom){
        roomPlayersRepository.deleteAllPlayersExceptModerator(idGameRoom);
    }
}
