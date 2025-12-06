package com.project.gamingplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "role_name",  nullable = false)
    @NotNull
    @Size(max = 50)
    private String roleName;

    @Column(name = "description")
    private String description;

    //связь с GameResults
    @OneToMany(mappedBy = "winnerRoleId")
    private List<GameResults> winnersGame = new ArrayList<>();

    //Связь с PlayerRoles
    @OneToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private List<PlayerRoles> playerRoles = new ArrayList<>();

    public Roles() {
    }

    public Roles(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GameResults> getWinnersGame() {
        return winnersGame;
    }

    public void setWinnersGame(List<GameResults> winnersGame) {
        this.winnersGame = winnersGame;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
