package com.project.gamingplatform.service;

import com.project.gamingplatform.entity.BunkerCards;
import com.project.gamingplatform.entity.CardType;
import com.project.gamingplatform.repository.BunkerCardsRepository;
import org.springframework.stereotype.Service;

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
    public Map<CardType, List<BunkerCards>> takeBunkerCards(int countPlayers){
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
}
