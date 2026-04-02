package com.project.gamingplatform.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServerMessage {
    // тип сообщения, для проверки на фронтенде
    private MessageType messageType;
//    одиночный пользователь
    private UsersDTO player;
//    список игроков (в случае определения победителя, обновления вариантов голосования и т.д)
    private List<UsersDTO> usersList;
}
