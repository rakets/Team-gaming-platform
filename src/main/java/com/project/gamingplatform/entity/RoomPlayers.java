package com.project.gamingplatform.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "room_players")
public class RoomPlayers {

    @EmbeddedId
    private RoomPlayersId id = new RoomPlayersId(); // составной ключ

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId")                               // связь с составным ключом
    @JoinColumn(name = "room_id")
    private GameRooms room;

    //Связь с Users
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")                               // связь с составным ключом
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "joined_at", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedAt;

    @Column(name = "is_ready", nullable = false)
    private Boolean isReady = false;

    @Column(name = "role_in_room")
    @Enumerated(EnumType.STRING)
    private RoleInRoom roleInRoom;

    public RoomPlayers() {
    }
    public RoomPlayers(GameRooms room, Users user, RoleInRoom roleInRoom) {
        this.room = room;
        this.user = user;
        this.roleInRoom = roleInRoom;
        this.id = new RoomPlayersId(room.getRoomId(), user.getUserId());
        this.joinedAt = new Date();
    }

    public RoomPlayersId getId() {
        return id;
    }

    public void setId(RoomPlayersId id) {
        this.id = id;
    }

    public GameRooms getRoom() {
        return room;
    }

    public void setRoom(GameRooms room) {
        this.room = room;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public Boolean getIsReady() {
        return isReady;
    }

    public void setIsReady(Boolean isReady) {
        this.isReady = isReady;
    }

    public RoleInRoom getRoleInRoom() {
        return roleInRoom;
    }

    public void setRoleInRoom(RoleInRoom roleInRoom) {
        this.roleInRoom = roleInRoom;
    }

    @Override
    public String toString() {
        return "RoomPlayers{" +
                "room=" + room +
                ", user=" + user +
                ", joinedAt=" + joinedAt +
                ", isReady=" + isReady +
                ", roleInRoom=" + roleInRoom +
                '}';
    }
}
