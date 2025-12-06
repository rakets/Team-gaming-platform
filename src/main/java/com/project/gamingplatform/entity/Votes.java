package com.project.gamingplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "votes")
public class Votes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Integer voteId;

    @Column(name="round_num", nullable = false)
    @NotNull
    private int roundNum;

    @Column(name = "created_at", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    //Связь с GameSessions
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private GameSessions session;

    //Связь с Users
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false)
    private Users userVoter;

    //Связь с Users
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Users userTarget;

    public Votes() {
    }

    public Votes(int roundNum, GameSessions session, Users userVoter, Users userTarget) {
        this.roundNum = roundNum;
        this.createdAt = new Date();
        this.session = session;
        this.userVoter = userVoter;
        this.userTarget = userTarget;
    }

    public Integer getVoteId() {
        return voteId;
    }

    public void setVoteId(Integer voteId) {
        this.voteId = voteId;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public GameSessions getSession() {
        return session;
    }

    public void setSession(GameSessions session) {
        this.session = session;
    }

    public Users getUserVoter() {
        return userVoter;
    }

    public void setUserVoter(Users userVoter) {
        this.userVoter = userVoter;
    }

    public Users getUserTarget() {
        return userTarget;
    }

    public void setUserTarget(Users userTarget) {
        this.userTarget = userTarget;
    }

    @Override
    public String toString() {
        return "Votes{" +
                "voteId=" + voteId +
                ", roundNum=" + roundNum +
                ", createdAt=" + createdAt +
                ", session=" + session +
                ", userVoter=" + userVoter +
                ", userTarget=" + userTarget +
                '}';
    }
}
