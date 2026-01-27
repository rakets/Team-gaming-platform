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

import java.util.ArrayList;
import java.util.List;

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

    public List<GameRoomsDTO> findAllGameRoomsByUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users users = userDetails.getUser();

        List<GameRooms> gameRoomsList = gameRoomsRepository.findAllByCreatedBy(users);

        List<GameRoomsDTO> gameRoomsDTOList = new ArrayList<GameRoomsDTO>();
        for(GameRooms gameRoom : gameRoomsList){
            GameRoomsDTO gameRoomDTO = new GameRoomsDTO();
            gameRoomDTO.setRoomId(gameRoom.getRoomId());
            gameRoomDTO.setRoomName(gameRoom.getRoomName());
            gameRoomDTO.setNumPlayers(gameRoom.getMaxPlayers());

            gameRoomsDTOList.add(gameRoomDTO);
        }
        return gameRoomsDTOList;
    }
}
