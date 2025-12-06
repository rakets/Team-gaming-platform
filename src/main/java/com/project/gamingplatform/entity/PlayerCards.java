package com.project.gamingplatform.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "player_cards")
public class PlayerCards {
    @EmbeddedId
    private PlayerCardsId id = new PlayerCardsId(); //составной ключ

    //Связь с GameSessions
    @ManyToOne(fetch = FetchType.LAZY)         //связь с составным ключем
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private GameSessions session;

    //Связь с Users
    @ManyToOne(fetch = FetchType.LAZY)         //связь с составным ключем
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    //Связь с BunkerCards
    @ManyToOne(fetch = FetchType.LAZY)         //связь с составным ключем
    @MapsId("bunkerCardId")
    @JoinColumn(name = "bunker_card_id")
    private BunkerCards bunkerCards;

    @Column(name = "revealed")
    private boolean revealed = false;

    public PlayerCards() {
    }

    public PlayerCards(GameSessions session, Users user, BunkerCards bunkerCards) {
        this.session = session;
        this.user = user;
        this.bunkerCards = bunkerCards;
        this.id = new PlayerCardsId(
                session.getSessionId(),
                user.getUserId(),
                bunkerCards.getCardId()
        );
    }

    public PlayerCardsId getId() {
        return id;
    }

    public GameSessions getSession() {
        return session;
    }

    public void setSession(GameSessions session) {
        this.session = session;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public BunkerCards getBunkerCards() {
        return bunkerCards;
    }

    public void setBunkerCards(BunkerCards bunkerCards) {
        this.bunkerCards = bunkerCards;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    @Override
    public String toString() {
        return "PlayerCards{" +
                "id=" + id +
                ", session=" + session +
                ", user=" + user +
                ", bunkerCards=" + bunkerCards +
                ", revealed=" + revealed +
                '}';
    }
}
