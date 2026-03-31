package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.GameResults;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultsRepository extends JpaRepository<GameResults, Integer> {
}
