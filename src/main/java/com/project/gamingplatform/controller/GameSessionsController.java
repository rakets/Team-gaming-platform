package com.project.gamingplatform.controller;

import com.project.gamingplatform.service.GameSessionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/gamesession")
@Slf4j
public class GameSessionsController {
    private final GameSessionsService gameSessionsService;

    public GameSessionsController(GameSessionsService gameSessionsService) {
        this.gameSessionsService = gameSessionsService;
    }

    @GetMapping("/{roomName}")
    public String getGameSession(Model model,
                                 @PathVariable("roomName") String roomName){
        model.addAttribute("roomName", roomName);
        return "gameSession";
    }

    @PostMapping("/join/{roomId}/{roomName}")
    public String createNewGameSession(@PathVariable("roomId") int roomId,
                                       @PathVariable("roomName") String roomName){
        gameSessionsService.createGameSession(roomId);
        return "redirect:/gamesession/" + roomName;
    }
}