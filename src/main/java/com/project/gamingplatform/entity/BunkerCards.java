package com.project.gamingplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bunker_cards")
public class BunkerCards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(name = "card_name")
    @Size(max = 50)
    private String cardName;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "category_set")
    @Size(max = 50)
    private String categorySet;

    //связь с PlayerCards
    @OneToMany(mappedBy = "bunkerCards", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerCards> playerCards = new ArrayList<>();

    public BunkerCards() {
    }

    public BunkerCards(CardType cardType, String cardName, String description, boolean isActive, String categorySet) {
        this.cardType = cardType;
        this.cardName = cardName;
        this.description = description;
        this.isActive = isActive;
        this.categorySet = categorySet;
    }

    public Integer getCardId() {
        return cardId;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType curdType) {
        this.cardType = curdType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(String categorySet) {
        this.categorySet = categorySet;
    }

    public List<PlayerCards> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(List<PlayerCards> playerCards) {
        this.playerCards = playerCards;
    }

    @Override
    public String toString() {
        return "BunkerCards{" +
                "cardId=" + cardId +
                ", cardType=" + cardType +
                ", cardName='" + cardName + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", categorySet='" + categorySet + '\'' +
                ", playerCards=" + playerCards +
                '}';
    }
}
