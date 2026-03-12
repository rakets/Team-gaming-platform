package com.project.gamingplatform.dto;

import com.project.gamingplatform.entity.CardType;
import lombok.Data;

@Data
public class BunkerCardList {
    private BunkerCardsDTO bunkerInfo;
    private BunkerCardsDTO catastrophe;
    private BunkerCardsDTO profession;
    private BunkerCardsDTO health;
    private BunkerCardsDTO hobby;
    private BunkerCardsDTO phobia;
    private BunkerCardsDTO luggage;
    private BunkerCardsDTO character;
    private BunkerCardsDTO skill;
}
