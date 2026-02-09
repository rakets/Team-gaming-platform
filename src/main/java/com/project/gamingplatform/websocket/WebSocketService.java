package com.project.gamingplatform.websocket;

import com.project.gamingplatform.dto.*;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.RoleInRoom;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.service.UsersService;
import com.project.gamingplatform.util.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final UsersService usersService;

    public WebSocketService(SimpMessagingTemplate messagingTemplate, UsersService usersService) {
        this.messagingTemplate = messagingTemplate;
        this.usersService = usersService;
    }

//    public void createAndSendMessage() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        Users user = userDetails.getUser();
//
//        UserActivityDTO joinMessage = new UserActivityDTO();
//        joinMessage.setUsername(user.getUsername());
//        if (gameRoomsDTO.getCreatedBy().equals(user.getUserId())) {
//            joinMessage.setRoleInRoom(RoleInRoom.MODERATOR);
//        } else {
//            joinMessage.setRoleInRoom(RoleInRoom.PLAYER);
//        }
//        messagingTemplate.convertAndSend("/topic/room/" + id, joinMessage);
//    }

//    public void playerIsReady(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        Users user = userDetails.getUser();
//
//        UserActivityDTO joinMessage = new UserActivityDTO();
//        joinMessage.setUsername(user.getUsername());
//        if (gameRoomsDTO.getCreatedBy().equals(user.getUserId())) {
//            joinMessage.setRoleInRoom(RoleInRoom.MODERATOR);
//        } else {
//            joinMessage.setRoleInRoom(RoleInRoom.PLAYER);
//        }
//        messagingTemplate.convertAndSend("/topic/room/" + id, joinMessage);
//    }

    // уведомляем всех в комнате, что игрок вошел
    public void joinToGameRoom(GameRoomsDTO gameRoomsDTO){
        UsersDTO usersDTO = usersService.getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);
        usersDTO.setMessageType(MessageType.JOIN);
        messagingTemplate.convertAndSend("/topic/room/" + gameRoomsDTO.getRoomId(), usersDTO);
    }

    // уведомляем всех в комнате, что игрок ready
    public void userIsReady(UsersDTO user, int roomId){
        user.setReadyStatus(ReadyStatus.READY);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, user);
    }
}
