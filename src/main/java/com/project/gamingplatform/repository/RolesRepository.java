package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
    @Query("SELECT r FROM Roles r WHERE r.roleName = :role")
    Roles findRolesByRoleName(@Param("role") String role);
//    Roles findRolesByRoleName(@Param("role") RolesInGameSession role);
}
