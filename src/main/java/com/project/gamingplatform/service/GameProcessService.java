package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.BunkerCardList;
import com.project.gamingplatform.dto.PlayerCardsDTO;
import com.project.gamingplatform.entity.*;
import com.project.gamingplatform.repository.*;
import com.project.gamingplatform.websocket.WebSocketService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GameProcessService {
    private final BunkerCardsService bunkerCardsService;
    private final RoomPlayersRepository roomPlayersRepository;
    private final PlayerCardsRepository playerCardsRepository;
    private final RolesRepository rolesRepository;
    private final PlayerRolesRepository playerRolesRepository;
    private final BunkerCardsRepository bunkerCardsRepository;
    private final WebSocketService webSocketService;

    public GameProcessService(BunkerCardsService bunkerCardsService, RoomPlayersRepository roomPlayersRepository, PlayerCardsRepository playerCardsRepository, RolesRepository rolesRepository, PlayerRolesRepository playerRolesRepository, BunkerCardsRepository bunkerCardsRepository, WebSocketService webSocketService) {
        this.bunkerCardsService = bunkerCardsService;
        this.roomPlayersRepository = roomPlayersRepository;
        this.playerCardsRepository = playerCardsRepository;
        this.rolesRepository = rolesRepository;
        this.playerRolesRepository = playerRolesRepository;
        this.bunkerCardsRepository = bunkerCardsRepository;
        this.webSocketService = webSocketService;
    }

    //рандомная раздача игровых карт игрокам
    public void distributionGameCards(GameSessions gameSessions) {
        List<RoomPlayers> roomPlayersList = roomPlayersRepository.findAllRoomPlayersByRoomId(gameSessions.getGameRooms().getRoomId());
        Map<CardType, List<BunkerCards>> cards = bunkerCardsService.takeBunkerCards(roomPlayersList.size());
        List<BunkerCards> profession = cards.get(CardType.PROFESSION);
        List<BunkerCards> health = cards.get(CardType.HEALTH);
        List<BunkerCards> hobby = cards.get(CardType.HOBBY);
        List<BunkerCards> phobia = cards.get(CardType.PHOBIA);
        List<BunkerCards> luggage = cards.get(CardType.LUGGAGE);
        List<BunkerCards> character = cards.get(CardType.CHARACTER);
        List<BunkerCards> skill = cards.get(CardType.SKILL);

        BunkerCards bunkerInfo = cards.get(CardType.BUNKER_INFO).getFirst();
        BunkerCards catastrophe = cards.get(CardType.CATASTROPHE).getFirst();

        // check room players
        List<RoomPlayers> newRoomPlayersList = new ArrayList<>();
        for (RoomPlayers roomPlayer : roomPlayersList){
            if(!playerRolesRepository.existsBySessionAndUser(gameSessions, roomPlayer.getUser())) {
                newRoomPlayersList.add(roomPlayer);
            }
        }
        if(newRoomPlayersList.isEmpty()){
            return;
        }

        //list of all game session cards
        List<PlayerCards> playerCardsList = new ArrayList<>();
        //add profession cards in list
        addCardsInList(playerCardsList, newRoomPlayersList, profession, gameSessions);
        addCardsInList(playerCardsList, newRoomPlayersList, health, gameSessions);
        addCardsInList(playerCardsList, newRoomPlayersList, hobby, gameSessions);
        addCardsInList(playerCardsList, newRoomPlayersList, phobia, gameSessions);
        addCardsInList(playerCardsList, newRoomPlayersList, luggage, gameSessions);
        addCardsInList(playerCardsList, newRoomPlayersList, character, gameSessions);
        addCardsInList(playerCardsList, newRoomPlayersList, skill, gameSessions);

        //add Bunker Info and Catastrophe for each player
        for (RoomPlayers roomPlayer : newRoomPlayersList){
            PlayerCards playerCardBunkerInfo = new PlayerCards(gameSessions, roomPlayer.getUser(), bunkerInfo);
            PlayerCards playerCardCatastrophe = new PlayerCards(gameSessions, roomPlayer.getUser(), catastrophe);
            playerCardsList.add(playerCardBunkerInfo);
            playerCardsList.add(playerCardCatastrophe);
        }

        // save all player cards
        playerCardsRepository.saveAll(playerCardsList);
        log.info("Player cards has been saving successfully");
        // distributin and saving roles
//        distributionGameRolesInBunker(gameSessions, roomPlayersList);
        distributionGameRolesInBunker(gameSessions, newRoomPlayersList);
    }

    private void addCardsInList(List<PlayerCards> playerCardsList,
                                List<RoomPlayers> roomPlayersList,
                                List<BunkerCards> bunkerCardsList,
                                GameSessions gameSessions) {

        for (int i = 0; i < roomPlayersList.size(); i++) {
            PlayerCards playerCards = new PlayerCards();
            playerCards.setUser(roomPlayersList.get(i).getUser());
            playerCards.setBunkerCards(bunkerCardsList.get(i));
            playerCards.setSession(gameSessions);
            playerCardsList.add(playerCards);
        }
    }

    // присваивание всем игрокам роли Survivour
    private void distributionGameRolesInBunker(GameSessions gameSession,
                                              List<RoomPlayers> roomPlayersList) {
//        Roles role = rolesRepository.findRolesByRoleName(RolesInGameSession.SURVIVOR);
        Roles role = rolesRepository.findRolesByRoleName("Survivour");

        //list of all game roles
        List<PlayerRoles> playerRolesList = new ArrayList<>();
        for (RoomPlayers roomPlayer : roomPlayersList) {
            PlayerRoles playerRole = new PlayerRoles();
            playerRole.setSession(gameSession);
            playerRole.setUser(roomPlayer.getUser());
            playerRole.setRoles(role);
            playerRolesList.add(playerRole);
        }
        playerRolesRepository.saveAll(playerRolesList);
        log.info("Roles has been saving successfully");
    }

    //изменение статуса показанной карты и оправка ее всем игрокам
    @Transactional
    public void showCard(PlayerCardsDTO card) {
        playerCardsRepository.updateRevealed(card.getUserId(),card.getBunkerCard().getCardId());
        List<BunkerCards> bunkerCardsList = bunkerCardsRepository.getBunkerCardsByUserIdRoomIdRevealedStatus(card.getUserId(), card.getRoomId(), true);
        BunkerCardList bunkerCards = bunkerCardsService.getBunkerCardsDTOByUserIdRoomId(bunkerCardsList);
        Map<String, Object> data = new HashMap<>();
        data.put("userId", card.getUserId());
        data.put("cards", bunkerCards);
        webSocketService.showCard(data, card.getRoomId());
    }
}


