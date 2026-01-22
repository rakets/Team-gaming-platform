package com.project.gamingplatform.controller;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.service.RoomService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dashboard")
@Slf4j
public class GameRoomsController {

    private final RoomService roomService;

    @Autowired
    public GameRoomsController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/room/new")
    public String registerNewRoom(Model model){
        model.addAttribute("room", new GameRoomsDTO());
        return "createRoom";
    }

    @PostMapping("/room/new")
    public String roomRegistration(@Valid @ModelAttribute("room") GameRoomsDTO newRoom,
                                   BindingResult result,
                                   Model model){
        if(result.hasErrors()){
            return "createRoom";
        }
        else {
            log.info("room " + newRoom.getRoomName() + " with " + newRoom.getNumPlayers() + " players have been created");
            roomService.saveGameRoom(newRoom);
            return "redirect:/dashboard/";
        }
    }
}
