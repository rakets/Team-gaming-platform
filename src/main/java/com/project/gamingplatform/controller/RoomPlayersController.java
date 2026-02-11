package com.project.gamingplatform.controller;

import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.service.GameRoomsService;
import com.project.gamingplatform.service.RoomPlayersService;
import com.project.gamingplatform.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roomPlayers")
public class RoomPlayersController {
    private final GameRoomsService gameRoomsService;

    public RoomPlayersController(GameRoomsService gameRoomsService) {
        this.gameRoomsService = gameRoomsService;
    }

    @PatchMapping("/ready/{roomId}")
    public ResponseEntity<Void> ready(@PathVariable int roomId) {
        gameRoomsService.userIsReady(roomId);
        return ResponseEntity.ok().build(); // пустой ответ, 200 OK
    }

    @PatchMapping("/unready/{roomId}")
    public ResponseEntity<Void> unReady(@PathVariable int roomId) {
        gameRoomsService.userIsUnReady(roomId);
        return ResponseEntity.ok().build(); // пустой ответ, 200 OK
    }
}
