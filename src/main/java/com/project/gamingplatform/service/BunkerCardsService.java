package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.BunkerCardList;
import com.project.gamingplatform.dto.BunkerCardsDTO;
import com.project.gamingplatform.entity.BunkerCards;
import com.project.gamingplatform.entity.CardType;
import com.project.gamingplatform.repository.BunkerCardsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BunkerCardsService {
    private final BunkerCardsRepository bunkerCardsRepository;

    public BunkerCardsService(BunkerCardsRepository bunkerCardsRepository) {
        this.bunkerCardsRepository = bunkerCardsRepository;
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

    public List<BunkerCards> getBunkerCardsByUserIdRoomId(int userId, int roomId) {
        return bunkerCardsRepository.getBunkerCardsByUserIdRoomId(userId, roomId);
    }

//    public List<BunkerCardsDTO> getBunkerCardsDTOByUserIdRoomId(int userId, int roomId) {
//        List<BunkerCards> bunkerCardsList = getBunkerCardsByUserIdRoomId(userId, roomId);
//        List<BunkerCardsDTO> bunkerCardsDTOList = new ArrayList<>();
//        for (BunkerCards bunkerCard : bunkerCardsList) {
//            BunkerCardsDTO bunkerCardsDTO = convertEntityToDTO(bunkerCard);
//            bunkerCardsDTOList.add(bunkerCardsDTO);
//        }
//        return bunkerCardsDTOList;
//    }

    public BunkerCardList getBunkerCardsDTOByUserIdRoomId(int userId, int roomId) {
        List<BunkerCards> bunkerCardsList = getBunkerCardsByUserIdRoomId(userId, roomId);
        BunkerCardList bunkerCardList = new BunkerCardList();
        for (BunkerCards bunkerCard : bunkerCardsList) {
            BunkerCardsDTO bunkerCardsDTO = convertEntityToDTO(bunkerCard);
            switch (bunkerCardsDTO.getCardType()) {
                case CardType.BUNKER_INFO:
                    bunkerCardList.setBunkerInfo(bunkerCardsDTO);
                    break;
                case CardType.CATASTROPHE:
                    bunkerCardList.setCatastrophe(bunkerCardsDTO);
                    break;
                case CardType.PROFESSION:
                    bunkerCardList.setProfession(bunkerCardsDTO);
                    break;
                case CardType.HEALTH:
                    bunkerCardList.setHealth(bunkerCardsDTO);
                    break;
                case CardType.HOBBY:
                    bunkerCardList.setHobby(bunkerCardsDTO);
                    break;
                case CardType.PHOBIA:
                    bunkerCardList.setPhobia(bunkerCardsDTO);
                    break;
                case CardType.LUGGAGE:
                    bunkerCardList.setLuggage(bunkerCardsDTO);
                    break;
                case CardType.CHARACTER:
                    bunkerCardList.setCharacter(bunkerCardsDTO);
                    break;
                case CardType.SKILL:
                    bunkerCardList.setSkill(bunkerCardsDTO);
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
}
