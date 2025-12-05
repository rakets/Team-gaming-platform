package com.project.gamingplatform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayerRolesId implements Serializable {
    @Column(name = "session_id")
    private Integer sessionId;
    @Column(name = "user_id")
    private Integer userId;

    public PlayerRolesId() {
    }

    public PlayerRolesId(Integer sessionId, Integer userId) {
        this.sessionId = sessionId;
        this.userId = userId;
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

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (!(o instanceof PlayerRolesId)) return false;
        PlayerRolesId that = (PlayerRolesId) o;
        return Objects.equals(sessionId, that.sessionId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, userId);
    }
}
