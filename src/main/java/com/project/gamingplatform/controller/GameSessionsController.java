package com.project.gamingplatform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.gamingplatform.dto.ChatMessageDTO;
import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.service.ChatService;
import com.project.gamingplatform.service.GameRoomsService;
import com.project.gamingplatform.service.GameSessionsService;
import com.project.gamingplatform.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/game-session")
@Slf4j
public class GameSessionsController {
    private final GameSessionsService gameSessionsService;
    private final GameRoomsService gameRoomsService;
    private final UsersService usersService;
    private final ChatService chatService;

    public GameSessionsController(GameSessionsService gameSessionsService, GameRoomsService gameRoomsService, UsersService usersService, ChatService chatService) {
        this.gameSessionsService = gameSessionsService;
        this.gameRoomsService = gameRoomsService;
        this.usersService = usersService;
        this.chatService = chatService;
    }

    @GetMapping("/join/{roomId}/{roomName}")
    public String getGameSession(Model model,
                                 @PathVariable("roomId") int roomId,
                                 @PathVariable("roomName") String roomName)  throws JsonProcessingException {
        GameRoomsDTO gameRoomsDTO = gameRoomsService.joinToGameRoom(roomId);
        List<UsersDTO> usersDTOList = usersService.findAllUsersByGameRoom(gameRoomsDTO);
        UsersDTO currentUser = usersService.getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);

        List<ChatMessageDTO> chatHistory = chatService.getChatHistory(roomId);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("players", usersDTOList);
        model.addAttribute("room", gameRoomsDTO);
        model.addAttribute("chatHistory", chatHistory);
        return "gameSession";
    }

    @PatchMapping("/join/{roomId}/{roomName}")
    public ResponseEntity<Void> getGameSession(@PathVariable("roomId") int roomId){
        gameSessionsService.updateGameSessionStatus(SessionGameStatus.IN_PROGRESS, roomId);
        return ResponseEntity.ok().build();
    }
}