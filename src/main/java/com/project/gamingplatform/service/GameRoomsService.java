package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class GameRoomsService {
    private final GameRoomsRepository gameRoomsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public GameRoomsService(GameRoomsRepository gameRoomsRepository, UsersRepository usersRepository) {
        this.gameRoomsRepository = gameRoomsRepository;
        this.usersRepository = usersRepository;
    }

    public void saveGameRoom(GameRoomsDTO room){
        GameRooms gameRoom = new GameRooms();

        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
//        Users users = usersRepository.findByUsername(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("User not found"));
        CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
        Users users = userDetail.getUser();

        gameRoom.setCreatedBy(users);
        gameRoom.setRoomName(room.getRoomName());
        gameRoom.setMaxPlayers(room.getNumPlayers());

        gameRoomsRepository.save(gameRoom);
    }
}
