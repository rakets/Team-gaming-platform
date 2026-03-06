package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameSessions;
import com.project.gamingplatform.entity.PlayerRoles;
import com.project.gamingplatform.entity.PlayerRolesId;
import com.project.gamingplatform.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRolesRepository extends JpaRepository<PlayerRoles, PlayerRolesId> {
    boolean existsBySessionAndUser(GameSessions session, Users user);
    void deleteAllBySession(GameSessions gameSession);
}
