package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.*;
import com.project.gamingplatform.repository.PlayerCardsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GameProcessService {
    private final BunkerCardsService bunkerCardsService;
    private final RoomPlayersRepository roomPlayersRepository;
    private final PlayerCardsRepository playerCardsRepository;

    public GameProcessService(BunkerCardsService bunkerCardsService, RoomPlayersRepository roomPlayersRepository, PlayerCardsRepository playerCardsRepository) {
        this.bunkerCardsService = bunkerCardsService;
        this.roomPlayersRepository = roomPlayersRepository;
        this.playerCardsRepository = playerCardsRepository;
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

        //list of all game session cards
        List<PlayerCards> playerCardsList = new ArrayList<>();

        //add profession cards in list
        addCardsInList(roomPlayersList.size(), playerCardsList, roomPlayersList, profession, gameSessions);
        addCardsInList(roomPlayersList.size(), playerCardsList, roomPlayersList, health, gameSessions);
        addCardsInList(roomPlayersList.size(), playerCardsList, roomPlayersList, hobby, gameSessions);
        addCardsInList(roomPlayersList.size(), playerCardsList, roomPlayersList, phobia, gameSessions);
        addCardsInList(roomPlayersList.size(), playerCardsList, roomPlayersList, luggage, gameSessions);
        addCardsInList(roomPlayersList.size(), playerCardsList, roomPlayersList, character, gameSessions);
        addCardsInList(roomPlayersList.size(), playerCardsList, roomPlayersList, skill, gameSessions);

        playerCardsRepository.saveAll(playerCardsList);
    }

    private void addCardsInList(int count,
                                List<PlayerCards> playerCardsList,
                                List<RoomPlayers> roomPlayersList,
                                List<BunkerCards> bunkerCardsList,
                                GameSessions gameSessions){
        for (int i = 0; i < count; i++) {
            PlayerCards playerCards = new PlayerCards();
            playerCards.setUser(roomPlayersList.get(i).getUser());
            playerCards.setBunkerCards(bunkerCardsList.get(i));
            playerCards.setSession(gameSessions);
            playerCardsList.add(playerCards);
        }
    }
}


