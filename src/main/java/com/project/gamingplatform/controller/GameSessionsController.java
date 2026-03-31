package com.project.gamingplatform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.gamingplatform.dto.*;
import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
    private final SimpMessagingTemplate messagingTemplate;
    private final VotesService votesService;
    private final GameSessionsService gameSessionsService;
    private final GameResultsService gameResultsService;

    public GameSessionsController(GameRoomsService gameRoomsService,
                                  UsersService usersService,
                                  ChatService chatService,
                                  GameProcessService gameProcessService,
                                  SimpMessagingTemplate messagingTemplate,
                                  VotesService votesService,
                                  GameSessionsService gameSessionsService,
                                  GameResultsService gameResultsService) {
        this.gameRoomsService = gameRoomsService;
        this.usersService = usersService;
        this.chatService = chatService;
        this.gameProcessService = gameProcessService;
        this.messagingTemplate = messagingTemplate;
        this.votesService = votesService;
        this.gameSessionsService = gameSessionsService;
        this.gameResultsService = gameResultsService;
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

    //метод реагирует, когда модератор нажимает 'VOTING RESULT' и возвращает пользователя
    @PatchMapping("/game-voting/{id}")
    public ResponseEntity<SessionGameStatus> votingResult(@PathVariable("id") int roomId) {
        UsersDTO user = votesService.getDeadPlayer(roomId);
        GameResultsResponse resultResponse = gameResultsService.getGameResult(roomId);
        //ситуация, если игрок имеет id 0 (не определен, голосование не прошло)
        if (user.getUserId().equals(0)) {
            messagingTemplate.convertAndSend("/topic/room/" + roomId, user);
            //возврат ответа, с статусом сессии IN_PROGRESS
            return ResponseEntity.badRequest().build();
        }
        //отправка выбывшего игрока всем игрокам
        messagingTemplate.convertAndSend("/topic/room/" + roomId, user);
        //если статус сессии FINISHED, то отправка всем результатов игры
        if (resultResponse.getGameStatus().equals(SessionGameStatus.FINISHED)) {
            messagingTemplate.convertAndSend("/topic/room/" + roomId, resultResponse);
        }
        //возврат ответа, с статусом сессии FINISHED/IN_PROGRESS и на фронтенде происходит обработка
        return ResponseEntity.ok(resultResponse.getGameStatus());
    }

    //метод реагирует, когда игрок нажимает 'VOTE'
    @PostMapping("/vote")
    public ResponseEntity<Void> newVote(@RequestBody VotesDTO vote) {
        System.out.println("Голос игрока ID " + vote.getUserId() + " на игрока ID " + vote.getVote() + " в комнате ID " + vote.getRoomId());
        if (votesService.newVote(vote)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    //метод срабатывает, когда MODERATOR нажимает 'NEXT ROUND'
    @PatchMapping("/next-round/{currentRound}/{roomId}")
    public ResponseEntity<Void> nextRound(@PathVariable("currentRound") int currentRound,
                                          @PathVariable("roomId") int roomId) {
        if (gameSessionsService.nextRound(currentRound, roomId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

//    @PatchMapping("/join/{roomId}/{roomName}")
//    public ResponseEntity<Void> getGameSession(@PathVariable("roomId") int roomId){
//        gameSessionsService.updateGameSessionStatus(SessionGameStatus.IN_PROGRESS, roomId);
//        return ResponseEntity.ok().build();
//    }
}