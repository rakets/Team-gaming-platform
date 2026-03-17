package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.PlayerCardsDTO;
import com.project.gamingplatform.entity.PlayerCards;
import com.project.gamingplatform.repository.PlayerCardsRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerCardsService {
    private final PlayerCardsRepository playerCardsRepository;

    public PlayerCardsService(PlayerCardsRepository playerCardsRepository) {
        this.playerCardsRepository = playerCardsRepository;
    }

    public void showCard(PlayerCardsDTO card) {
        playerCardsRepository.updateRevealed(card.getUserId(),card.getBunkerCard().getCardId());
    }
}
