package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.entity.*;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.util.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

        //create new RoomPlayer
        RoomPlayers roomPlayers = new RoomPlayers(savedGameRoom, user, RoleInRoom.MODERATOR);
        roomPlayersRepository.save(roomPlayers);
    }

    // connect to RoomPlayer as PLAYER
//    public void connectToRoomPlayer(){
//
//    }

}
