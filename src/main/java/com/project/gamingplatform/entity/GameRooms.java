package com.project.gamingplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "game_rooms")
public class GameRooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "room_name")
    @NotEmpty
    @Size(max = 50)
    private String roomName;

    @Column(name = "max_players")
    @Max(10)
    private Integer maxPlayers;

    @ManyToOne(fetch = FetchType.LAZY)                  // связь с пользователем
    @JoinColumn(name = "created_by", nullable = false)
    private Users createdBy;

    @Column(name = "created_at", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPlayers> roomPlayers = new ArrayList<>();

    public GameRooms() {
    }

    public GameRooms(String roomName, Integer maxPlayers, Users createdBy, Boolean isActive) {
        this.roomName = roomName;
        this.maxPlayers = maxPlayers;
        this.createdBy = createdBy;
        this.createdAt = new Date();
        this.isActive = isActive;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "GameRooms{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", maxPlayers=" + maxPlayers +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", isActive=" + isActive +
                '}';
    }
}
