package com.project.gamingplatform.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "game_results")
public class GameResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Integer resulId;

    //сохраняется через java, при обновлении записи
    @Column(name = "ended_at", updatable = true, insertable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endedAt;

    //связь с GameSession
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "session_id", unique = true, nullable = false, insertable = true, updatable = true)
//    private GameSessions gameSessions;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private GameSessions gameSessions;

    //связь с Roles
    @ManyToOne
    @JoinColumn(name = "winner_role_id", nullable = false)
    private Roles winnerRoleId;

    //связь с Users
    @ManyToOne
    @JoinColumn(name = "winner_user_id", nullable = true)  //В SQL — NULL ALLOWED
    private Users winnerUserId;

    public GameResults() {
    }

    public Integer getResulId() {
        return resulId;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public GameSessions getGameSessions() {
        return gameSessions;
    }

    public void setGameSessions(GameSessions gameSessions) {
        this.gameSessions = gameSessions;
    }

    public Roles getWinnerRoleId() {
        return winnerRoleId;
    }

    public void setWinnerRoleId(Roles winnerRoleId) {
        this.winnerRoleId = winnerRoleId;
    }

    public Users getWinnerUserId() {
        return winnerUserId;
    }

    public void setWinnerUserId(Users winnerUserId) {
        this.winnerUserId = winnerUserId;
    }

    @Override
    public String toString() {
        return "GameResultsRepository{" +
                "resulId=" + resulId +
                ", endedAt=" + endedAt +
                ", gameSessions=" + gameSessions +
                ", winnerRoleId=" + winnerRoleId +
                ", winnerUserId=" + winnerUserId +
                '}';
    }
}