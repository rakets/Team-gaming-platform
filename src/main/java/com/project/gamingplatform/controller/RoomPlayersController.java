package com.project.gamingplatform.controller;

import com.project.gamingplatform.service.GameRoomsService;
import com.project.gamingplatform.service.RoomPlayersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roomPlayers")
@Slf4j
public class RoomPlayersController {
    private final GameRoomsService gameRoomsService;
    private final RoomPlayersService roomPlayersService;

    public RoomPlayersController(GameRoomsService gameRoomsService,
                                 RoomPlayersService roomPlayersService) {
        this.gameRoomsService = gameRoomsService;
        this.roomPlayersService = roomPlayersService;
    }

    @PatchMapping("/ready/{roomId}")
    public ResponseEntity<Void> ready(@PathVariable("roomId") int roomId) {
        gameRoomsService.userIsReady(roomId);
        return ResponseEntity.ok().build(); // пустой ответ, 200 OK
    }

    @PatchMapping("/unready/{roomId}")
    public ResponseEntity<Void> unReady(@PathVariable("roomId") int roomId) {
        gameRoomsService.userIsUnReady(roomId);
        return ResponseEntity.ok().build(); // пустой ответ, 200 OK
    }

    @PostMapping("/allReady/{roomId}")
    public ResponseEntity<Void> isAllPlayersReady(@PathVariable("roomId") int roomId){
        if(roomPlayersService.areAllUserReadyInRoom(roomId)){
            log.info("All players are ready in room ID: " + roomId);
            return ResponseEntity.ok().build();
        } else {
            // если вернуло false, то высылаем 409 Conflict
            log.info("Not all players are ready in room ID: " + roomId);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
