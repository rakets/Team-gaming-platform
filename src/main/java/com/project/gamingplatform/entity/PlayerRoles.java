package com.project.gamingplatform.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "player_roles")
public class PlayerRoles {

    @EmbeddedId
    private PlayerRolesId id = new PlayerRolesId(); //составной ключ

    //Связь с GameSessions
    @ManyToOne(fetch = FetchType.LAZY)      //связь с составным ключем
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private GameSessions session;

    //Связь с Users
    @ManyToOne(fetch = FetchType.LAZY)      //связь с составным ключем
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    //Связь с Roles
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Roles rolesId;

    @Column(name = "assigned_at", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedAt;

    public PlayerRoles() {
    }

    public PlayerRoles(GameSessions session, Users user) {
        this.id = new PlayerRolesId(session.getSessionId(), user.getUserId());
        this.session = session;
        this.user = user;
        this.assignedAt = new Date();
    }

    public PlayerRolesId getId() {
        return id;
    }

    public void setId(PlayerRolesId id) {
        this.id = id;
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

    public Roles getRolesId() {
        return rolesId;
    }

    public void setRolesId(Roles rolesId) {
        this.rolesId = rolesId;
    }

    public Date getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Date assignedAt) {
        this.assignedAt = assignedAt;
    }

    @Override
    public String toString() {
        return "PlayerRoles{" +
                "session=" + session +
                ", user=" + user +
                ", rolesId=" + rolesId +
                ", assignedAt=" + assignedAt +
                '}';
    }
}