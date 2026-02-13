package com.project.gamingplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    @NotEmpty
    @Size(max = 50)
    private String username;

    @Column(name = "email")
    @NotEmpty
    @Email
    private String email;

    @Column(name = "password")
    @NotEmpty
    private String password;

    @Column(name = "registration_date", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Column(name = "global_role")
    @Enumerated(EnumType.STRING)
    private GlobalRole globalRole = GlobalRole.PLAYER;

    @Column(name = "profile_image")
    private String profileImage;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<GameRooms> createdRooms = new ArrayList<>();

    //Связь с RoomPlayers
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomPlayers> roomPlayers = new ArrayList<>();

    //Связь с GameResults
    @OneToMany(mappedBy = "winnerUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameResults> gameResults = new ArrayList<>();

    //Связь с PlayerRoles
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerRoles> playerRoles = new ArrayList<>();

    //Связь с Votes
    @OneToMany(mappedBy = "userVoter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Votes> usersVoter = new ArrayList<>();
    //Связь с Votes
    @OneToMany(mappedBy = "userTarget", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Votes> usersTarget = new ArrayList<>();

    //Связь с PlayerCards
    //убрано cascade = CascadeType.ALL, orphanRemoval = true, потому что будет цепная реакция(
    // удаляешь пользователя → удаляются его PlayerCards → каскадно удалятся карты → каскадно удалится карточка в BunkerCards → каскадно удалятся другие PlayerCards: цепная реакция.)
    @OneToMany(mappedBy = "user")
    private List<PlayerCards> playerCards = new ArrayList<>();

    public Users() {
    }

    public Users(String username, String email, String password, boolean isActive, GlobalRole globalRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registrationDate = new Date();
        this.isActive = isActive;
        this.globalRole = globalRole;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public GlobalRole getGlobalRole() {
        return globalRole;
    }

    public void setGlobalRole(GlobalRole globalRole) {
        this.globalRole = globalRole;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registrationDate=" + registrationDate +
                ", isActive=" + isActive +
                ", globalRole=" + globalRole +
                ", profileImage='" + profileImage +
                '}';
    }
}


