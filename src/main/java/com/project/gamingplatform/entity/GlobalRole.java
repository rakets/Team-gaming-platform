package com.project.gamingplatform.entity;

public enum GlobalRole {
    ADMIN, PLAYER
}

//CREATE database `gameplatform`;
//
//USE `gameplatform`;
//
//CREATE TABLE `users`(
//        `user_id` INT NOT NULL AUTO_INCREMENT UNIQUE,
//        `username` VARCHAR(50) NOT NULL UNIQUE,
//    `email` VARCHAR(255) NOT NULL UNIQUE,
//    `password` VARCHAR(255) DEFAULT NULL,
//    `registration_date` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
//        `is_active` BOOLEAN DEFAULT FALSE,
//        `global_role` ENUM('ADMIN', 'PLAYER') DEFAULT 'PLAYER',
//        `profile_image` VARCHAR(255) DEFAULT NULL,
//PRIMARY KEY (`user_id`)
//) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
//
//CREATE TABLE `game_rooms`(
//        `room_id` INT NOT NULL AUTO_INCREMENT UNIQUE,
//        `room_name` VARCHAR(50) NOT NULL UNIQUE,
//    `max_players` INT DEFAULT 10,
//        `created_by` INT NOT NULL,
//        `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
//        `is_active` BOOLEAN DEFAULT FALSE,
//PRIMARY KEY (`room_id`),
//CONSTRAINT `FK_game_rooms_TO_users` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
//
//CREATE TABLE `room_players`(
//        `room_id` INT NOT NULL,
//        `user_id` INT NOT NULL,
//        `joined_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
//        `is_ready` BOOLEAN DEFAULT FALSE,
//        `role_in_room` ENUM('MODERATOR', 'PLAYER') DEFAULT 'PLAYER',
//PRIMARY KEY (`room_id`, `user_id`),
//CONSTRAINT `FK_room_players_TO_game_rooms` FOREIGN KEY (`room_id`) REFERENCES `game_rooms` (`room_id`),
//CONSTRAINT `FK_room_players_TO_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
//
//CREATE TABLE `game_sessions`(
//        `session_id` INT NOT NULL AUTO_INCREMENT UNIQUE,
//        `room_id` INT NOT NULL,
//        `started_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
//        `ended_at` DATETIME(6) DEFAULT NULL,
//    `status` ENUM('WAITING', 'IN_PROGRESS', 'FINISHED') DEFAULT 'WAITING',
//        `current_round` INT DEFAULT 1,
//PRIMARY KEY (`session_id`),
//CONSTRAINT `FK_game_sessions_TO_game_rooms` FOREIGN KEY (`room_id`) REFERENCES `game_rooms` (`room_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
//
//CREATE TABLE `votes`(
//        `vote_id` INT NOT NULL AUTO_INCREMENT UNIQUE,
//        `session_id` INT NOT NULL,
//        `voter_id` INT NOT NULL,
//        `target_id` INT NOT NULL,
//        `round_num` INT NOT NULL,
//        `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
//PRIMARY KEY (`vote_id`),
//UNIQUE KEY `UQ_vote_once` (`voter_id`, `session_id`, `round_num`),
//CONSTRAINT `FK_votes_TO_game_sessions` FOREIGN KEY (`session_id`) REFERENCES `game_sessions` (`session_id`),
//CONSTRAINT `FK_votes_TO_users` FOREIGN KEY (`voter_id`) REFERENCES `users` (`user_id`),
//CONSTRAINT `FK_votes_TO_target` FOREIGN KEY (`target_id`) REFERENCES `users` (`user_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
//
//CREATE TABLE `bunker_cards`(
//        `card_id` INT NOT NULL AUTO_INCREMENT UNIQUE,
//        `card_type` ENUM('PROFESSION', 'HEALTH', 'HOBBY', 'PHOBIA', 'LUGGAGE', 'CHARACTER', 'SKILL', 'BUNKER_INFO', 'CATASTROPHE') NOT NULL,
//    `card_name` VARCHAR(50) NOT NULL,
//    `description` TEXT,
//        `is_active` BOOLEAN DEFAULT TRUE,
//        `category_set` VARCHAR(50) NULL,
//PRIMARY KEY (`card_id`)
//) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
//
//CREATE TABLE `roles`(
//        `role_id` INT NOT NULL AUTO_INCREMENT UNIQUE,
//        `role_name` VARCHAR(50) NOT NULL,
//    `description` TEXT,
//PRIMARY KEY (`role_id`)
//) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
//
//CREATE TABLE `player_roles`(
//        `session_id` INT NOT NULL,
//        `user_id` INT NOT NULL,
//        `role_id` INT NOT NULL ,
//        `assigned_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
//PRIMARY KEY (`session_id`, `user_id`),
//CONSTRAINT `FK_player_roles_TO_game_sessions` FOREIGN KEY (`session_id`) REFERENCES `game_sessions` (`session_id`),
//CONSTRAINT `FK_player_roles_TO_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
//CONSTRAINT `FK_player_roles_TO_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
//
//CREATE TABLE `game_results`(
//        `result_id` INT NOT NULL AUTO_INCREMENT,
//    `session_id` INT NOT NULL,
//        `winner_role_id` INT NOT NULL,
//        `winner_user_id` INT,
//        `ended_at` DATETIME(6) DEFAULT NULL,
//PRIMARY KEY (`result_id`),
//CONSTRAINT `FK_game_results_TO_users` FOREIGN KEY (`winner_user_id`) REFERENCES `users` (`user_id`),
//CONSTRAINT `FK_game_results_TO_game_sessions` FOREIGN KEY (`session_id`) REFERENCES `game_sessions` (`session_id`),
//CONSTRAINT `FK_game_results_TO_roles` FOREIGN KEY (`winner_role_id`) REFERENCES `roles` (`role_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
//
//CREATE TABLE `player_cards`(
//        `session_id` INT NOT NULL,
//        `user_id` INT NOT NULL,
//        `bunker_card_id` INT NOT NULL,
//revealed BOOLEAN DEFAULT FALSE,
//PRIMARY KEY (`session_id`, `user_id`, `bunker_card_id`),
//CONSTRAINT `FK_player_cards_TO_game_sessions` FOREIGN KEY (`session_id`) REFERENCES `game_sessions` (`session_id`),
//CONSTRAINT `FK_player_cards_TO_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
//CONSTRAINT `FK_player_cards_TO_bunker_cards` FOREIGN KEY (`bunker_card_id`) REFERENCES `bunker_cards` (`card_id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


