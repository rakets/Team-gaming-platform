package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.BunkerCardList;
import com.project.gamingplatform.dto.BunkerCardsDTO;
import com.project.gamingplatform.dto.GameSessionInfo;
import com.project.gamingplatform.entity.BunkerCards;
import com.project.gamingplatform.entity.CardType;
import com.project.gamingplatform.entity.RoomPlayers;
import com.project.gamingplatform.repository.BunkerCardsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BunkerCardsService {
    private final BunkerCardsRepository bunkerCardsRepository;
    private final RoomPlayersRepository roomPlayersRepository;

    public BunkerCardsService(BunkerCardsRepository bunkerCardsRepository,
                              RoomPlayersRepository roomPlayersRepository) {
        this.bunkerCardsRepository = bunkerCardsRepository;
        this.roomPlayersRepository = roomPlayersRepository;
    }

    //получение всех игровых карт
    public Map<CardType, List<BunkerCards>> takeBunkerCards(int countPlayers) {
        Map<CardType, List<BunkerCards>> cards = new HashMap<>();
        cards.put(CardType.PROFESSION, bunkerCardsRepository.getBunkerCardsByCardType(CardType.PROFESSION, countPlayers));
        cards.put(CardType.HEALTH, bunkerCardsRepository.getBunkerCardsByCardType(CardType.HEALTH, countPlayers));
        cards.put(CardType.HOBBY, bunkerCardsRepository.getBunkerCardsByCardType(CardType.HOBBY, countPlayers));
        cards.put(CardType.PHOBIA, bunkerCardsRepository.getBunkerCardsByCardType(CardType.PHOBIA, countPlayers));
        cards.put(CardType.LUGGAGE, bunkerCardsRepository.getBunkerCardsByCardType(CardType.LUGGAGE, countPlayers));
        cards.put(CardType.CHARACTER, bunkerCardsRepository.getBunkerCardsByCardType(CardType.CHARACTER, countPlayers));
        cards.put(CardType.SKILL, bunkerCardsRepository.getBunkerCardsByCardType(CardType.SKILL, countPlayers));

        cards.put(CardType.BUNKER_INFO, bunkerCardsRepository.getBunkerCardsByCardType(CardType.BUNKER_INFO, 1));
        cards.put(CardType.CATASTROPHE, bunkerCardsRepository.getBunkerCardsByCardType(CardType.CATASTROPHE, 1));
        return cards;
    }

    //sorting game cards by CardType for player
    public BunkerCardList getBunkerCardsDTOByUserIdRoomId(List<BunkerCards> bunkerCardsList) {
        BunkerCardList bunkerCardList = new BunkerCardList();
        for (BunkerCards bunkerCard : bunkerCardsList) {
            switch (bunkerCard.getCardType()) {
                case CardType.PROFESSION:
                    bunkerCardList.setProfession(convertEntityToDTO(bunkerCard));
                    break;
                case CardType.HEALTH:
                    bunkerCardList.setHealth(convertEntityToDTO(bunkerCard));
                    break;
                case CardType.HOBBY:
                    bunkerCardList.setHobby(convertEntityToDTO(bunkerCard));
                    break;
                case CardType.PHOBIA:
                    bunkerCardList.setPhobia(convertEntityToDTO(bunkerCard));
                    break;
                case CardType.LUGGAGE:
                    bunkerCardList.setLuggage(convertEntityToDTO(bunkerCard));
                    break;
                case CardType.CHARACTER:
                    bunkerCardList.setCharacter(convertEntityToDTO(bunkerCard));
                    break;
                case CardType.SKILL:
                    bunkerCardList.setSkill(convertEntityToDTO(bunkerCard));
                    break;
            }
        }
        return bunkerCardList;
    }

    // cards about game session
    public GameSessionInfo getGameSessionInfoCards(List<BunkerCards> bunkerCardsList) {
        GameSessionInfo bunkerCardList = new GameSessionInfo();
        for (BunkerCards bunkerCard : bunkerCardsList) {
            switch (bunkerCard.getCardType()) {
                case CardType.BUNKER_INFO:
                    bunkerCardList.setBunkerInfo(convertEntityToDTO(bunkerCard));
                    break;
                case CardType.CATASTROPHE:
                    bunkerCardList.setCatastrophe(convertEntityToDTO(bunkerCard));
                    break;
            }
        }
        return bunkerCardList;
    }

    private BunkerCardsDTO convertEntityToDTO(BunkerCards bunkerCard) {
        BunkerCardsDTO bunkerCardsDTO = new BunkerCardsDTO();
        bunkerCardsDTO.setCardId(bunkerCard.getCardId());
        bunkerCardsDTO.setCardType(bunkerCard.getCardType());
        bunkerCardsDTO.setCardName(bunkerCard.getCardName());
        bunkerCardsDTO.setDescription(bunkerCard.getDescription());
        return bunkerCardsDTO;
    }

    // getting cards with revealed = 1 for all players in the room
    public Map<Integer, BunkerCardList> getAllPlayersRevealedBunkerCardsInRoom(int roomId) {
        List<RoomPlayers> roomPlayersList = roomPlayersRepository.findAllRoomPlayersByRoomId(roomId);
        Map<Integer, BunkerCardList> allPlayersBunkerCards = new HashMap<>();
        for (RoomPlayers roomPlayer : roomPlayersList) {
            int playerId = roomPlayer.getUser().getUserId();
            List<BunkerCards> bunkerCardsList = bunkerCardsRepository.getBunkerCardsByUserIdRoomIdRevealedStatus(playerId, roomId, true);

            allPlayersBunkerCards.put(playerId, getBunkerCardsDTOByUserIdRoomId(bunkerCardsList));
        }
        return allPlayersBunkerCards;
    }

//    //    получить cards всех игроков, кроме тебя
//    public Map<Integer, BunkerCardList> getAllPlayersBunkerCardsDTOInRoom(int roomId, int userId){
//        List<RoomPlayers> roomPlayersList = roomPlayersRepository.findAllRoomPlayersByRoomId(roomId);
//        Map<Integer, BunkerCardList> allPlayersBunkerCards = new HashMap<>();
//        for (RoomPlayers roomPlayers: roomPlayersList) {
//            // проверка актуальный ли игрок
//            Integer playerId = roomPlayers.getUser().getUserId();
//            if(!playerId.equals(userId)){
//                allPlayersBunkerCards.put(playerId, getBunkerCardsDTOByUserIdRoomId(playerId, roomId));
//            }
//        }
//        System.out.println("Bunker cards: " + allPlayersBunkerCards);
//        return allPlayersBunkerCards;
//    }
}
