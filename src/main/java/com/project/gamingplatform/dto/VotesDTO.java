package com.project.gamingplatform.dto;

import lombok.Data;

@Data
public class VotesDTO {
    private Integer userId;
    private Integer vote;
    private Integer roomId;
}
