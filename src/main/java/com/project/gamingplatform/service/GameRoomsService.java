package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.*;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.GameSessionsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.util.CustomUserDetails;
import com.project.gamingplatform.websocket.WebSocketService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
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
    private final RoomPlayersService roomPlayersService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UsersService usersService;
    private final WebSocketService webSocketService;
    private final GameSessionsRepository gameSessionsRepository;
    private final RoomPlayersRepository roomPlayersRepository;

    public GameRoomsService(GameRoomsRepository gameRoomsRepository,
                            RoomPlayersService roomPlayersService,
                            SimpMessagingTemplate messagingTemplate,
                            UsersService usersService,
                            @Lazy WebSocketService webSocketService,
                            GameSessionsRepository gameSessionsRepository,
                            RoomPlayersRepository roomPlayersRepository) {
        this.gameRoomsRepository = gameRoomsRepository;
        this.roomPlayersService = roomPlayersService;
        this.messagingTemplate = messagingTemplate;
        this.usersService = usersService;
        this.webSocketService = webSocketService;
        this.gameSessionsRepository = gameSessionsRepository;
        this.roomPlayersRepository = roomPlayersRepository;
    }

    @Transactional
    public void saveGameRoom(GameRoomsDTO room) {
        GameRooms gameRoom = convertGameRoomDtoToEntity(room);
        //saving to GameRooms
        GameRooms currentGameRoom = gameRoomsRepository.save(gameRoom);
        //saving to RoomPlayers
        roomPlayersService.saveRoomPlayers(currentGameRoom);

        //saving to GameSession
        GameSessions gameSessions = new GameSessions(currentGameRoom);
        gameSessionsRepository.save(gameSessions);
    }

    //получение всех комнат игрока
    public List<GameRoomsDTO> findAllGameRoomsByUser(Users currentUser ) {
        List<GameRooms> gameRoomsList = gameRoomsRepository.findAllByCreatedBy(currentUser);
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

//------------- если вдруг понадобится понимать этот пользователь PLAYER или MODERATOR ----------
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        Users users = userDetails.getUser();
//        if (users.getUserId().equals(gameRoomsDTO.getCreatedBy())) {
//            gameRoomsDTO.setCurrentUserModerator(true);
//        }
// ------------- если вдруг понадобится понимать этот пользователь PLAYER или MODERATOR ----------
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
    public GameRoomsDTO joinToGameRoom(int roomId) {
        //take current game room from bd
        GameRooms gameRoom = gameRoomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("The room was not found when the player tried to join the room."));
        roomPlayersService.joinToRoomPlayer(gameRoom); //join to room
        GameRoomsDTO gameRoomsDTO = findGameRoomById(roomId);
        // проверка, если все игроки READY и session уже начата (для отображения кнопки JOIN Game Session)
        if(roomPlayersRepository.isAllUserReadyInRoom(roomId) && gameSessionsRepository.existsByGameRoomsRoomIdAndStatus(roomId, SessionGameStatus.IN_PROGRESS)) {
            gameRoomsDTO.setAllUsersIsReady(true);
        } else {
            gameRoomsDTO.setAllUsersIsReady(false);
        }
        GameSessions gameSessions = gameSessionsRepository.getGameSessionsByRoomId(roomId);
        gameRoomsDTO.setCurrentRound(gameSessions.getCurrentRound());
        webSocketService.joinToGameRoom(gameRoomsDTO); //websocket

        return gameRoomsDTO;
    }

    //  !!!!  ПЕРЕНЕСТИ В RoomPlayersService
    //ОТ RoomPlayersController -> пометка пользователя как ready -> RoomPlayersService.userIsReady
    public void userIsReady(int roomId) {
        // получение комнаты и пользователя
        GameRoomsDTO gameRoomsDTO = findGameRoomById(roomId);
        UsersDTO usersDTO = usersService.getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);
        // сохранение ready в бд
        roomPlayersService.userIsReady(usersDTO.getUserId(), gameRoomsDTO.getRoomId());
        //websocket
        webSocketService.userIsReady(usersDTO, roomId);
    }
    //  !!!!  ПЕРЕНЕСТИ В RoomPlayersService
    public void userIsUnReady(int roomId) {
        // получение комнаты и пользователя
        GameRoomsDTO gameRoomsDTO = findGameRoomById(roomId);
        UsersDTO usersDTO = usersService.getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);
        // сохранение ready в бд
        roomPlayersService.userIsUnready(usersDTO.getUserId(), gameRoomsDTO.getRoomId());
        //websocket
        webSocketService.userIsUnReady(usersDTO, roomId);
    }
}