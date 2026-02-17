package com.project.gamingplatform.service;

import com.project.gamingplatform.entity.*;
import com.project.gamingplatform.repository.GameRoomsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.util.CustomUserDetails;
import com.project.gamingplatform.websocket.WebSocketService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoomPlayersService {
    private final RoomPlayersRepository roomPlayersRepository;
    private final GameRoomsRepository gameRoomsRepository;
    private final WebSocketService webSocketService;

    public RoomPlayersService(RoomPlayersRepository roomPlayersRepository,
                              GameRoomsRepository gameRoomsRepository,
                              WebSocketService webSocketService) {
        this.roomPlayersRepository = roomPlayersRepository;
        this.gameRoomsRepository = gameRoomsRepository;
        this.webSocketService = webSocketService;
    }

    // save RoomPlayer and adding as MODERATOR
    @Transactional
    public void saveRoomPlayers(GameRooms savedGameRoom) {
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
    public void joinToRoomPlayer(GameRooms gameRoom) {
        //take current user from context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users user = userDetails.getUser();
        if (roomPlayersRepository.existsByRoomAndUser(gameRoom, user)) {
            return;
        }
        if (gameRoom.getCreatedBy().getUserId().equals(user.getUserId())) {
            //save new RoomPlayer as PLAYER
            RoomPlayers roomPlayers = new RoomPlayers(gameRoom, user, RoleInRoom.MODERATOR);
            roomPlayersRepository.save(roomPlayers);
            log.info("User " + user.getUsername() + " with ID: " + user.getUserId() + " join room № " + gameRoom.getRoomId() + " as MODERATOR");
        } else {
            //save new RoomPlayer as PLAYER
            RoomPlayers roomPlayers = new RoomPlayers(gameRoom, user, RoleInRoom.PLAYER);
            roomPlayersRepository.save(roomPlayers);
            log.info("User " + user.getUsername() + " with ID: " + user.getUserId() + " join room № " + gameRoom.getRoomId() + " as PLAYER");
        }
    }

    @Transactional
    public void cleanRoomPlayers(int roomId, int userId) {
        roomPlayersRepository.deleteAllPlayersExceptModerator(roomId);
        //установка статуса UNREADY у модератора
        roomPlayersRepository.updateUserAsUnready(userId, roomId);
    }

    // ОТ GameRoomsService -> бд
    @Transactional
    public void userIsReady(int userId, int roomId) {
        // сохранение ready в бд
        roomPlayersRepository.updateUserAsReady(userId, roomId);
    }

    @Transactional
    public void userIsUnready(int userId, int roomId) {
        // сохранение ready в бд
        roomPlayersRepository.updateUserAsUnready(userId, roomId);
    }

    public List<RoomPlayers> findAllRoomPlayersByRoomId(int roomId) {
        List<RoomPlayers> roomPlayersList = roomPlayersRepository.findAllRoomPlayersByRoomId(roomId);
        return roomPlayersList;
    }

    public boolean isUserReadyInRoom(int roomId, int userId) {
        return roomPlayersRepository.isUserReadyInRoom(roomId, userId);
    }

    public boolean areAllUserReadyInRoom(int roomId) {
        if (roomPlayersRepository.isAllUserReadyInRoom(roomId)){
            webSocketService.joinGameSession(roomId);
            return true;
        } else {
            return false;
        }
    }

//    @Transactional
//    public void deleteUserFromGameRoom(int roomId, int userId) {
//        RoomPlayersId roomPlayersId = new RoomPlayersId(roomId, userId);
//        roomPlayersRepository.deleteById(roomPlayersId);
//        log.info("User " + userId + " is deleted from RoomPlayers.");
//    }
}
