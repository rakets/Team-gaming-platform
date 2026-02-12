package com.project.gamingplatform.websocket;

import com.project.gamingplatform.dto.*;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.RoleInRoom;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.service.UsersService;
import com.project.gamingplatform.util.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;

@Service
@Slf4j
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final UsersService usersService;
    private final RoomPlayersRepository roomPlayersRepository; // Или сервис

    public WebSocketService(SimpMessagingTemplate messagingTemplate, UsersService usersService, RoomPlayersRepository roomPlayersRepository) {
        this.messagingTemplate = messagingTemplate;
        this.usersService = usersService;
        this.roomPlayersRepository = roomPlayersRepository;
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

    public void userIsUnReady(UsersDTO user, int roomId){
        user.setReadyStatus(ReadyStatus.UNREADY);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, user);
    }

    // Слушаем событие отключения
    @EventListener
    @Transactional
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // достаем roomId из сессии (куда мы его положили в Config)
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        if (sessionAttributes != null) {
            String roomIdStr = (String) sessionAttributes.get("roomId");
            if (roomIdStr != null) {
                int roomId = Integer.parseInt(roomIdStr);
                String username = event.getUser().getName(); // Если пользователь авторизован

                System.out.println("User " + username + " disconnected from room " +  roomId);
            }
        }
    }
}
