package com.project.gamingplatform.controller;

import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.service.GameSessionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/game-session")
@Slf4j
public class GameSessionsController {
    private final GameSessionsService gameSessionsService;

    public GameSessionsController(GameSessionsService gameSessionsService) {
        this.gameSessionsService = gameSessionsService;
    }

    @GetMapping("/join/{roomId}/{roomName}")
    public String getGameSession(Model model,
                                 @PathVariable("roomId") int roomId,
                                 @PathVariable("roomName") String roomName){
        model.addAttribute("roomName", roomName);
        return "gameSession";
    }

    @PatchMapping("/join/{roomId}/{roomName}")
    public ResponseEntity<Void> createNewGameSession(@PathVariable("roomId") int roomId){
        gameSessionsService.updateGameSessionStatus(SessionGameStatus.IN_PROGRESS, roomId);
        return ResponseEntity.ok().build();
    }
}