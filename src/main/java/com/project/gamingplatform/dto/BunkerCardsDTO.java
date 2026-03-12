package com.project.gamingplatform.dto;

import com.project.gamingplatform.entity.CardType;
import lombok.Data;

@Data
public class BunkerCardsDTO {
    private Integer cardId;
    private CardType cardType;
    private String cardName;
    private String description;
}
