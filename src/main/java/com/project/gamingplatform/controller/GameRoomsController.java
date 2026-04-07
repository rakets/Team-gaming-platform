package com.project.gamingplatform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.gamingplatform.dto.ChatMessageDTO;
import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.SessionGameStatus;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.service.*;
import com.project.gamingplatform.util.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
@Slf4j
public class GameRoomsController {

    private final GameRoomsService gameRoomsService;
    private final RoomPlayersService roomPlayersService;
    private final UsersService usersService;
    private final ChatService chatService;
    private final GameSessionsService gameSessionsService;

    public GameRoomsController(GameRoomsService gameRoomsService,
                               RoomPlayersService roomPlayersService,
                               UsersService usersService,
                               ChatService chatService,
                               GameSessionsService gameSessionsService) {
        this.gameRoomsService = gameRoomsService;
        this.roomPlayersService = roomPlayersService;
        this.usersService = usersService;
        this.chatService = chatService;
        this.gameSessionsService = gameSessionsService;
    }

    @GetMapping("/room/new")
    public String registerNewRoom(Model model) {
        model.addAttribute("room", new GameRoomsDTO());
        return "createRoom";
    }

    @PostMapping("/room/new")
    public String roomRegistration(@Valid @ModelAttribute("room") GameRoomsDTO newRoom,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            return "createRoom";
        } else {
            log.info("room " + newRoom.getRoomName() + " with " + newRoom.getNumPlayers() + " players have been created");
            gameRoomsService.saveGameRoom(newRoom);
            return "redirect:/dashboard/";
        }
    }

    @GetMapping("/myRoom")
    public String getAllRoomsByUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users currentUser = userDetails.getUser();
        List<GameRoomsDTO> gameRoomsDTOList = gameRoomsService.findAllGameRoomsByUser(currentUser);
        model.addAttribute("currentUserId", currentUser.getUserId());
        model.addAttribute("rooms", gameRoomsDTOList);
        return "roomsList";
    }

    @GetMapping("/searchRoom")
    public String searchGameRoom(Model model,
                                 @RequestParam(value = "roomName", required = false) String roomName) {
        log.info("Search initiated for room : room {}", roomName);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Users currentUser = userDetails.getUser();
        GameRoomsDTO gameRoomsDTO = gameRoomsService.findGameRoomByRoomName(roomName);
        model.addAttribute("currentUserId", currentUser.getUserId());
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("roomName", roomName);
        model.addAttribute("room", gameRoomsDTO);
        return "dashboard";
    }

    @GetMapping("/join-room/{roomId}/{userId}")
    public String joinGameRoom(Model model,
                               @PathVariable("roomId") int roomId,
                               @PathVariable("userId") int userId) throws JsonProcessingException {
        // получение статуса сессии и находится ли игрок в списке игроков, когда сессия запущена.
        Map<String, Object> statuses = gameSessionsService.isGameSessionInProgressAndPlayerInList(roomId, userId);
        // если игрок в списке игроков и сессия в статусе IN_PROGRESS, то игрок сразу перемещается в сессию
        if ((boolean) statuses.get("isUserCanJOIN")) {
            return "redirect:/game-session/join/" + roomId + "/" + userId;
        }
        // если сессия в статусе WAITING, то игрок перемещается в лобби(game room)
        if (statuses.get("sessionStatus") == SessionGameStatus.WAITING) {
            GameRoomsDTO gameRoomsDTO = gameRoomsService.joinToGameRoom(roomId);
            List<UsersDTO> usersDTOList = usersService.findAllUsersByGameRoom(gameRoomsDTO);
            UsersDTO currentUser = usersService.getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);
            List<ChatMessageDTO> chatHistory = chatService.getChatHistory(roomId);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("players", usersDTOList);
            model.addAttribute("room", gameRoomsDTO);
            model.addAttribute("chatHistory", chatHistory);
            return "gameRoom";
        }
        // если ни одно условие не выполнено, то игрок перемещается в окно поиска комнаты
        return "redirect:/dashboard/";
    }

    @DeleteMapping("/clean-room/{roomId}/{userId}")
    public String cleanGameRoom(@PathVariable("roomId") int roomId, @PathVariable("userId") int userId){
        roomPlayersService.cleanRoomPlayers(roomId, userId);
        return "redirect:/dashboard/join-room/" + roomId + "/" + userId;
    }

//    public UserActivityDTO addUser(@DestinationVariable("id") int id,
//    @MessageMapping("/room/{id}/newUser")
//    @SendTo("/topic/room/{id}")
//    public void addUser(@DestinationVariable("id") int roomId,
//                                   @Payload UserActivityDTO message,
//                                   SimpMessageHeaderAccessor headerAccessor){
//        // Сохраняем данные в сессию сокета, чтобы достать их при дисконнекте
//        headerAccessor.getSessionAttributes().put("username", message.getUsername());
//        headerAccessor.getSessionAttributes().put("roomId", roomId);
//
//        message.setType(MessageType.JOIN);
//
//        System.out.println(String.format("room id: %n %s player %s", roomId, message.getType(), message.getUsername()));
//    }
}
