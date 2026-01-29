package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.util.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GameRoomsService {
    private final GameRoomsRepository gameRoomsRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public GameRoomsService(GameRoomsRepository gameRoomsRepository, UsersRepository usersRepository) {
        this.gameRoomsRepository = gameRoomsRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
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
            gameRoomsDTOList.add(convertEntityGameRoomToDto(gameRoom));
        }
        return gameRoomsDTOList;
    }

    public GameRoomsDTO findGameRoomByRoomName(String roomName){
        //old version
//        Optional<GameRooms> gameRooms = gameRoomsRepository.findByRoomName(roomName);
//        if(gameRooms.isPresent()){
//            GameRoomsDTO gameRoomsDTO = convertEntityGameRoomToDto(gameRooms.get());
//            return gameRoomsDTO;
//        } else {
//            return null;
//        }

        //new version
        return gameRoomsRepository.findByRoomName(roomName)
                .map(gameRooms -> convertEntityGameRoomToDto(gameRooms))
                .orElse(null);
    }

    //method for convert GameRooms to GameRoomsDTO
    private GameRoomsDTO convertEntityGameRoomToDto(GameRooms gameRoom){
        GameRoomsDTO gameRoomsDTO = new GameRoomsDTO();
        gameRoomsDTO.setRoomId(gameRoom.getRoomId());
        gameRoomsDTO.setRoomName(gameRoom.getRoomName());
        gameRoomsDTO.setNumPlayers(gameRoom.getMaxPlayers());
        return gameRoomsDTO;
    }
}
