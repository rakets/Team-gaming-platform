package com.project.gamingplatform.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "game_sessions")
public class GameSessions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer sessionId;

    //сохраняется на бд, при сохранении записи
    @Column(name = "started_at", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedAt;

    //сохраняется через java, при обновлении записи
    @Column(name = "ended_at", updatable = true, insertable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SessionGameStatus status = SessionGameStatus.WAITING;

    @Column(name = "current_round")
    private int currentRound = 1;

    //связь с GameRooms
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private GameRooms gameRooms;

    //связь с GameResult
//    @OneToOne(mappedBy = "gameSessions")
//    private GameResults gameResults;
    @OneToMany(mappedBy = "gameSessions", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameResults> gameResults = new ArrayList<>();

    //Связь с PlayerRoles
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerRoles> playerRoles = new ArrayList<>();

    //Связь с Votes
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Votes> votes = new ArrayList<>();

    //связь с PlayerCards
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerCards> playerCards = new ArrayList<>();

    public GameSessions(GameRooms gameRooms) {
        this.gameRooms = gameRooms;
    }

    public GameSessions() {
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public SessionGameStatus getStatus() {
        return status;
    }

    public void setStatus(SessionGameStatus status) {
        this.status = status;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public GameRooms getGameRooms() {
        return gameRooms;
    }

    public void setGameRooms(GameRooms gameRooms) {
        this.gameRooms = gameRooms;
    }

    @Override
    public String toString() {
        return "GameSessions{" +
                "sessionId=" + sessionId +
                ", startedAt=" + startedAt +
                ", endedAt=" + endedAt +
                ", status=" + status +
                ", currentRound=" + currentRound +
                ", gameRooms=" + gameRooms +
                '}';
    }
}
