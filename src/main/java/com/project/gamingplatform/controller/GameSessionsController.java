package com.project.gamingplatform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.gamingplatform.dto.*;
import com.project.gamingplatform.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/game-session")
@Slf4j
public class GameSessionsController {
    private final GameRoomsService gameRoomsService;
    private final UsersService usersService;
    private final ChatService chatService;
    private final GameProcessService gameProcessService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public GameSessionsController(GameRoomsService gameRoomsService, UsersService usersService, ChatService chatService, GameProcessService gameProcessService, SimpMessagingTemplate simpMessagingTemplate) {
        this.gameRoomsService = gameRoomsService;
        this.usersService = usersService;
        this.chatService = chatService;
        this.gameProcessService = gameProcessService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    //    @GetMapping("/join/{roomId}/{userId}")
//    public String getGameSession(Model model,
//                                 @PathVariable("roomId") int roomId,
//                                 @PathVariable("userId") int userId)  throws JsonProcessingException {
////        List<BunkerCardsDTO> bunkerCardsDTOList = bunkerCardsService.getBunkerCardsDTOByUserIdRoomId(userId, roomId);
//        BunkerCardList myBunkerCards = bunkerCardsService.getBunkerCardsDTOByUserIdRoomId(userId, roomId);
//        Map<Integer, BunkerCardList> otherPlayersBunkerCards = bunkerCardsService.getAllPlayersBunkerCardsDTOInRoom(roomId, userId);
//
//        GameRoomsDTO gameRoomsDTO = gameRoomsService.joinToGameRoom(roomId);
//        List<UsersDTO> usersDTOList = usersService.findAllUsersByGameRoom(gameRoomsDTO);
//        UsersDTO currentUser = usersService.getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);
//
//        List<ChatMessageDTO> chatHistory = chatService.getChatHistory(roomId);
//

    /// /      model.addAttribute("cards", bunkerCardsDTOList);
//        model.addAttribute("otherCards", otherPlayersBunkerCards);
//        model.addAttribute("myCards", myBunkerCards);
//        model.addAttribute("currentUser", currentUser);
//        model.addAttribute("players", usersDTOList);
//        model.addAttribute("room", gameRoomsDTO);
//        model.addAttribute("chatHistory", chatHistory);
//        return "gameSession";
//    }
    @GetMapping("/join/{roomId}/{userId}")
    public String getGameSession(Model model,
                                 @PathVariable("roomId") int roomId,
                                 @PathVariable("userId") int currentUserId) throws JsonProcessingException {
        GameRoomsDTO gameRoomsDTO = gameRoomsService.joinToGameRoom(roomId);
        List<UsersDTO> usersDTOList = usersService.preparePlayersWithRevealedCards(gameRoomsDTO); //getting all players with revealed cards
        List<ChatMessageDTO> chatHistory = chatService.getChatHistory(roomId);
        UsersDTO currentUser = usersService.prepareCurrentPlayerWithCards(gameRoomsDTO); //getting current user with cards
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("players", usersDTOList);
        model.addAttribute("room", gameRoomsDTO);
        model.addAttribute("chatHistory", chatHistory);
        return "gameSession";
    }

    // получение выбранной карты через WebSocket
    @MessageMapping("/show.card")
    public void processDisplayCardsFromClient(PlayerCardsDTO card) throws JsonProcessingException {
        gameProcessService.showCard(card); //изменение статуса карты и возврат по WebSocket
    }

//    @PatchMapping("/join/{roomId}/{roomName}")
//    public ResponseEntity<Void> getGameSession(@PathVariable("roomId") int roomId){
//        gameSessionsService.updateGameSessionStatus(SessionGameStatus.IN_PROGRESS, roomId);
//        return ResponseEntity.ok().build();
//    }
}