package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.PlayerCards;
import com.project.gamingplatform.entity.PlayerCardsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerCardsRepository extends JpaRepository<PlayerCards, PlayerCardsId> {
}
