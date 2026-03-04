package com.project.gamingplatform.repository;

import com.project.gamingplatform.entity.BunkerCards;
import com.project.gamingplatform.entity.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BunkerCardsRepository extends JpaRepository<BunkerCards, Integer> {
    @Query("SELECT c from BunkerCards c WHERE c.cardType = :cardType ORDER BY RAND() LIMIT :count")
    List<BunkerCards> getBunkerCardsByCardType(@Param("cardType") CardType cardType,@Param("count") int count);
}
