package com.project.gamingplatform.dto;

import lombok.Data;

@Data
// класс показанной игровой карты
public class PlayerCardsDTO {
    private BunkerCardsDTO bunkerCard;
    private Integer roomId;
    private Integer userId;
}
