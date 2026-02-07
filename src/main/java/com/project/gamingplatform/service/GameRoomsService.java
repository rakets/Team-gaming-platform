package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.dto.MessageType;
import com.project.gamingplatform.dto.UserActivityDTO;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.RoleInRoom;
import com.project.gamingplatform.entity.RoomPlayers;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.util.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GameRoomsService {
    private final GameRoomsRepository gameRoomsRepository;
    private final UsersRepository usersRepository;
    private final RoomPlayersService roomPlayersService;
    private final GameSessionsService gameSessionsService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameRoomsService(GameRoomsRepository gameRoomsRepository, UsersRepository usersRepository, RoomPlayersService roomPlayersService, GameSessionsService gameSessionsService, SimpMessagingTemplate messagingTemplate) {
        this.gameRoomsRepository = gameRoomsRepository;
        this.usersRepository = usersRepository;
        this.roomPlayersService = roomPlayersService;
        this.gameSessionsService = gameSessionsService;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public void saveGameRoom(GameRoomsDTO room) {
        GameRooms gameRoom = convertGameRoomDtoToEntity(room);
        //saving to GameRooms
        GameRooms currentGameRoom = gameRoomsRepository.save(gameRoom);
        //saving to RoomPlayers
        roomPlayersService.saveRoomPlayers(currentGameRoom);
        //saving to GameSessions
        gameSessionsService.saveGameSession(gameRoom);
    }

    public List<GameRoomsDTO> findAllGameRoomsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users users = userDetails.getUser();

        List<GameRooms> gameRoomsList = gameRoomsRepository.findAllByCreatedBy(users);

        List<GameRoomsDTO> gameRoomsDTOList = new ArrayList<GameRoomsDTO>();
        for (GameRooms gameRoom : gameRoomsList) {
            gameRoomsDTOList.add(convertEntityGameRoomToDto(gameRoom));
        }
        return gameRoomsDTOList;
    }

    public GameRoomsDTO findGameRoomByRoomName(String roomName) {
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

    public GameRoomsDTO findGameRoomById(int id) {
        GameRooms gameRooms = gameRoomsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("The room was not found, when user tried search room."));
        GameRoomsDTO gameRoomsDTO = convertEntityGameRoomToDto(gameRooms);
        return gameRoomsDTO;
    }

    //method for convert entity GameRooms to GameRoomsDTO
    public GameRoomsDTO convertEntityGameRoomToDto(GameRooms gameRoom) {
        GameRoomsDTO gameRoomsDTO = new GameRoomsDTO();
        gameRoomsDTO.setRoomId(gameRoom.getRoomId());
        gameRoomsDTO.setRoomName(gameRoom.getRoomName());
        gameRoomsDTO.setNumPlayers(gameRoom.getMaxPlayers());
        gameRoomsDTO.setCreatedBy(gameRoom.getCreatedBy().getUserId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users users = userDetails.getUser();
        if (users.getUserId().equals(gameRoomsDTO.getCreatedBy())) {
            gameRoomsDTO.setCurrentUserModerator(true);
        }
        return gameRoomsDTO;
    }

    //method for convert GameRoomsDTO to entity GameRooms
    public GameRooms convertGameRoomDtoToEntity(GameRoomsDTO gameRoomsDTO) {
        GameRooms gameRoom = new GameRooms();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
        Users users = userDetail.getUser();

        gameRoom.setCreatedBy(users);
        gameRoom.setRoomName(gameRoomsDTO.getRoomName());
        gameRoom.setMaxPlayers(gameRoomsDTO.getNumPlayers());
        return gameRoom;
    }

    @Transactional
    public GameRoomsDTO joinToGameRoom(int id){
        //take current game room from bd
        GameRooms gameRoom = gameRoomsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("The room was not found when the player tried to join the room."));
        //join to room
        roomPlayersService.joinToRoomPlayer(gameRoom);

        GameRoomsDTO gameRoomsDTO = findGameRoomById(id);

        //        ------  websocket ---------
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();

        UserActivityDTO joinMessage = new UserActivityDTO();
        joinMessage.setUsername(user.getUsername());
        if(gameRoomsDTO.getCreatedBy().equals(user.getUserId())){
            joinMessage.setRoleInRoom(RoleInRoom.MODERATOR);
        } else {
            joinMessage.setRoleInRoom(RoleInRoom.PLAYER);
        }
        messagingTemplate.convertAndSend("/topic/room/" + id, joinMessage);
        //        ------  websocket ---------
        return gameRoomsDTO;
    }



}
