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
    List<BunkerCards> getBunkerCardsByCardType(@Param("cardType") CardType cardType, @Param("count") int count);

    // getting all cards
    @Query(value = "SELECT c.* FROM bunker_cards c " +
            "JOIN player_cards pc ON c.card_id = pc.bunker_card_id " +
            "JOIN users u ON pc.user_id = u.user_id " +
            "JOIN game_sessions gs ON pc.session_id = gs.session_id " +
            "JOIN game_rooms gr ON gs.room_id = gr.room_id " +
            "WHERE u.user_id = :userId AND gr.room_id = :roomId",
            nativeQuery = true)
    List<BunkerCards> getBunkerCardsByUserIdRoomId(@Param("userId")int userId,
                                                   @Param("roomId")int roomId);

    // getting revealed/unrevealed cards
    @Query(value = "SELECT c.* FROM bunker_cards c " +
            "JOIN player_cards pc ON c.card_id = pc.bunker_card_id " +
            "JOIN users u ON pc.user_id = u.user_id " +
            "JOIN game_sessions gs ON pc.session_id = gs.session_id " +
            "JOIN game_rooms gr ON gs.room_id = gr.room_id " +
            "WHERE u.user_id = :userId AND gr.room_id = :roomId AND pc.revealed = :status",
            nativeQuery = true)
    List<BunkerCards> getBunkerCardsByUserIdRoomIdRevealedStatus(@Param("userId")int userId,
                                                                 @Param("roomId")int roomId,
                                                                 @Param("status")boolean revealedStatus);
}
