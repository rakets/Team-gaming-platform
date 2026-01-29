package com.project.gamingplatform.controller;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.service.GameRoomsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public GameRoomsController(GameRoomsService gameRoomsService) {
        this.gameRoomsService = gameRoomsService;
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
}
