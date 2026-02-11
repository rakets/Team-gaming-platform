package com.project.gamingplatform.controller;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.service.GameRoomsService;
import com.project.gamingplatform.service.RoomPlayersService;
import com.project.gamingplatform.service.UsersService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@Slf4j
public class GameRoomsController {

    private final GameRoomsService gameRoomsService;
    private final RoomPlayersService roomPlayersService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UsersService usersService;

    @Autowired
    public GameRoomsController(GameRoomsService gameRoomsService, RoomPlayersService roomPlayersService, SimpMessagingTemplate messagingTemplate, UsersService usersService) {
        this.gameRoomsService = gameRoomsService;
        this.roomPlayersService = roomPlayersService;
        this.messagingTemplate = messagingTemplate;
        this.usersService = usersService;
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
        List<GameRoomsDTO> gameRoomsDTOList = gameRoomsService.findAllGameRoomsByUser();
        model.addAttribute("rooms", gameRoomsDTOList);
        return "roomsList";
    }

    @GetMapping("/searchRoom")
    public String searchGameRoom(Model model,
                                 @RequestParam(value = "roomName", required = false) String roomName) {
        log.info("Search initiated for room : room {}", roomName);
        GameRoomsDTO gameRoomsDTO = gameRoomsService.findGameRoomByRoomName(roomName);
        model.addAttribute("roomName", roomName);
        model.addAttribute("room", gameRoomsDTO);
        return "dashboard";
    }

    @GetMapping("/join-room/{id}")
    public String joinGameRoom(Model model,
                               @PathVariable("id") int id){
        GameRoomsDTO gameRoomsDTO = gameRoomsService.joinToGameRoom(id);
        List<UsersDTO> usersDTOList = usersService.findAllUsersByGameRoom(gameRoomsDTO);
        UsersDTO currentUser = usersService.getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("players", usersDTOList);
        model.addAttribute("room", gameRoomsDTO);
        return "gameRoom";
    }

    @DeleteMapping("/clean-room/{roomId}/{userId}")
    public String cleanGameRoom(@PathVariable("roomId") int roomId, @PathVariable("userId") int userId){
        roomPlayersService.cleanRoomPlayers(roomId, userId);
        return "redirect:/dashboard/join-room/" + roomId;
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
