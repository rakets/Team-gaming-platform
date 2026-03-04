package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.PlayerRoles;
import com.project.gamingplatform.entity.PlayerRolesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRolesRepository extends JpaRepository<PlayerRoles, PlayerRolesId> {
}
