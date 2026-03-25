package com.project.gamingplatform.dto;

import lombok.Data;

@Data
public class VoteResult {
    private Integer targetId;
    private Long count;

    public VoteResult(Integer targetId, Long count) {
        this.targetId = targetId;
        this.count = count;
    }
}
