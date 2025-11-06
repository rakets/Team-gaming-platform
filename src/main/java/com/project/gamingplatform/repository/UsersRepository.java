package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> { }

