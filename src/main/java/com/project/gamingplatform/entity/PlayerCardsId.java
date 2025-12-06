package com.project.gamingplatform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayerCardsId implements Serializable {
    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "bunker_card_id")
    private Integer bunkerCardId;

    public PlayerCardsId() {
    }

    public PlayerCardsId(Integer sessionId, Integer userId, Integer bunkerCardId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.bunkerCardId = bunkerCardId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBunkerCardId() {
        return bunkerCardId;
    }

    public void setBunkerCardId(Integer bunkerCardId) {
        this.bunkerCardId = bunkerCardId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (!(o instanceof PlayerCardsId)) return false;
        PlayerCardsId that = (PlayerCardsId) o;
        return Objects.equals(sessionId, that.sessionId) && Objects.equals(userId, that.userId) && Objects.equals(bunkerCardId, that.bunkerCardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, userId, bunkerCardId);
    }
}
