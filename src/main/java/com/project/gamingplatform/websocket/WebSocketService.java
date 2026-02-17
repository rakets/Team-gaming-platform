package com.project.gamingplatform.websocket;

import com.project.gamingplatform.dto.*;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.RoleInRoom;
import com.project.gamingplatform.entity.RoomPlayersId;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.service.GameRoomsService;
import com.project.gamingplatform.service.RoomPlayersService;
import com.project.gamingplatform.service.UsersService;
import com.project.gamingplatform.util.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final UsersService usersService;
    private final RoomPlayersRepository roomPlayersRepository;
    private final TaskScheduler taskScheduler;

    // хранилище задач на удаление: Key = UserId, Value = Задача таймера
    private final Map<Integer, ScheduledFuture<?>> pendingRemovals = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate,
                            @Lazy UsersService usersService,
                            RoomPlayersRepository roomPlayersRepository,
                            @Qualifier("heartBeatScheduler") TaskScheduler taskScheduler) {
        this.messagingTemplate = messagingTemplate;
        this.usersService = usersService;
        this.roomPlayersRepository = roomPlayersRepository;
        this.taskScheduler = taskScheduler;
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
    public void joinToGameRoom(GameRoomsDTO gameRoomsDTO) {
        UsersDTO usersDTO = usersService.getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);
        usersDTO.setMessageType(MessageType.JOIN);
        messagingTemplate.convertAndSend("/topic/room/" + gameRoomsDTO.getRoomId(), usersDTO);
    }

    // уведомляем всех в комнате, что игрок вышел
    public void leaveGameRoom(Integer roomId, Integer userId) {
        UsersDTO userWhoLeft = usersService.findById(userId);
        userWhoLeft.setMessageType(MessageType.LEAVE);
        log.info("Sending LEAVE message for user {} to room {}", userId, roomId);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, userWhoLeft);
    }

    // уведомляем всех в комнате, что игрок ready
    public void userIsReady(UsersDTO user, int roomId) {
        user.setReadyStatus(ReadyStatus.READY);
        user.setMessageType(MessageType.READY);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, user);
    }

    public void userIsUnReady(UsersDTO user, int roomId) {
        user.setReadyStatus(ReadyStatus.UNREADY);
        user.setMessageType(MessageType.UNREADY);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, user);
    }

    //уведомление, для появления кнопки join game session
    public void joinGameSession(int roomId){
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setMessageType(MessageType.JOIN_GAME_SESSION);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, usersDTO);
    }

    // отмена удаления (вызываем из WebSocketConfig при входе игрока)
    public void cancelPendingRemoval(Integer userId) {
        ScheduledFuture<?> task = pendingRemovals.get(userId);
        if (task != null) {
            task.cancel(false); // Останавливаем таймер
            pendingRemovals.remove(userId);
            log.info("User {} reconnected within timeout. Removal canceled.", userId);
        }
    }

    // Слушаем событие отключения
    @EventListener
    @Transactional
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // достаем roomId из сессии (куда мы его положили в Config)
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        log.info("sessionAttributes: " + sessionAttributes);
        if (sessionAttributes != null) {
            Integer roomId = (Integer) sessionAttributes.get("roomId");
            Integer userId = (Integer) sessionAttributes.get("userId");
            String gameRole = sessionAttributes.get("gameRole").toString();
            if (roomId != null && userId != null && gameRole != null) {
                String username = event.getUser().getName(); // Если пользователь авторизован
                log.info("User " + username + " with ID: " + userId + " disconnected from room " + roomId + " as " + gameRole);
                leaveGameRoom(roomId, userId);// оповещение по вебсокет, что игрок вышел
                if (gameRole.equals("PLAYER")) {
                    //запуск таймера.
                    ScheduledFuture<?> task = taskScheduler.schedule(() -> {
                        //сработает через 5сек, если не будет ответа.
                        log.info("Timeout passed. Removing user {} from room {}", userId, roomId);
                        if (gameRole.equals("PLAYER")) {
                            //----удаление юзера из таблицы
//                            roomPlayersService.deleteUserFromGameRoom(roomId, userId); //удаление игрока
                            RoomPlayersId roomPlayersId = new RoomPlayersId(roomId, userId);
                            roomPlayersRepository.deleteById(roomPlayersId);
                            log.info("User " + userId + " is deleted from RoomPlayers.");
                            pendingRemovals.remove(userId); // очистка карты
                        }
                    }, Instant.now().plusSeconds(5)); // время ожидания
                    pendingRemovals.put(userId, task); // cохраняем задачу, чтобы иметь возможность её отменить
                }
            }
        }
    }
}
