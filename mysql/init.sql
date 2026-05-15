CREATE DATABASE  IF NOT EXISTS `gameplatformnew` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gameplatformnew`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: gameplatformnew
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bunker_cards`
--

DROP TABLE IF EXISTS `bunker_cards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bunker_cards` (
  `card_id` int NOT NULL AUTO_INCREMENT,
  `card_type` enum('PROFESSION','HEALTH','HOBBY','PHOBIA','LUGGAGE','CHARACTER','SKILL','BUNKER','CATASTROPHE') NOT NULL,
  `card_name` varchar(50) NOT NULL,
  `description` text,
  `is_active` tinyint(1) DEFAULT '1',
  `category_set` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`card_id`),
  UNIQUE KEY `card_id` (`card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bunker_cards`
--

LOCK TABLES `bunker_cards` WRITE;
/*!40000 ALTER TABLE `bunker_cards` DISABLE KEYS */;
INSERT INTO `bunker_cards` VALUES (1,'PROFESSION','Lekarz','Może pomagać innym graczom w leczeniu chorób.',1,'PROFESSION'),(2,'HEALTH','Astma','Wymaga inhalatora, ma ograniczoną wydolność.',1,'HEALTH'),(3,'HOBBY','Szachy','Dobrze planuje ruchy i przewiduje decyzje innych.',1,'HOBBY'),(4,'PHOBIA','Lęk wysokości','Nie może przebywać w wysokich miejscach.',1,'PHOBIA'),(5,'LUGGAGE','Scyzoryk','Może być użyty do napraw i samoobrony.',1,'LUGGAGE'),(6,'CHARACTER','Lider','Potrafi motywować i kierować grupą.',1,'CHARACTER'),(7,'SKILL','Gotowanie','Umie przygotować jedzenie z ograniczonych zasobów.',1,'SKILL'),(8,'BUNKER','Zapas wody na 2 miesiące','Bunker posiada wodę pitną.',1,'BUNKER'),(9,'CATASTROPHE','Zagłada nuklearna','Powierzchnia jest skażona promieniowaniem.',1,'CATASTROPHE'),(10,'PROFESSION','Inżynier','Specjalista od napraw i systemów podtrzymywania życia.',1,'PROFESSION'),(11,'PROFESSION','Rolnik','Zna się na uprawie roślin w trudnych warunkach.',1,'PROFESSION'),(12,'PROFESSION','Nauczyciel','Przekazuje wiedzę i pomaga zachować porządek społeczny.',1,'PROFESSION'),(13,'PROFESSION','Żołnierz','Zapewnia bezpieczeństwo i zna techniki przetrwania.',1,'PROFESSION'),(20,'HEALTH','Doskonała kondycja','Rzadko choruje и może pracować ciężej niż inni.',1,'HEALTH'),(21,'HEALTH','Cukrzyca','Wymaga regularnych dostaw insuliny.',1,'HEALTH'),(22,'HEALTH','Bezsenność','Ma problemy z regeneracją sił po pracy.',1,'HEALTH'),(23,'HEALTH','Złamanie nogi','Porusza się powoli i wymaga opieki.',1,'HEALTH'),(30,'HOBBY','Majsterkowanie','Potrafi stworzyć coś z niczego.',1,'HOBBY'),(31,'HOBBY','Ogrodnictwo','Hobby, które pomaga w produkcji żywności.',1,'HOBBY'),(32,'HOBBY','Pierwsza pomoc','Zna podstawowe zabiegi ratujące życie.',1,'HOBBY'),(33,'HOBBY','Wędkarstwo','Cierpliwość i umiejętność zdobywania pożywienia.',1,'HOBBY'),(40,'PHOBIA','Klaustrofobia','Bunker jest dla tej osoby ogromnym wyzwaniem.',1,'PHOBIA'),(41,'PHOBIA','Achluofobia','Paraliżujący lęk przed ciemnością.',1,'PHOBIA'),(42,'PHOBIA','Mizofobia','Lęk przed brudem i bakteriami.',1,'PHOBIA'),(43,'PHOBIA','Hematofobia','Mdleje na widok krwi.',1,'PHOBIA'),(50,'LUGGAGE','Latarka','Niezbędna w przypadku awarii zasilania.',1,'LUGGAGE'),(51,'LUGGAGE','Zestaw nasion','Szansa na odnowienie zasobów żywności.',1,'LUGGAGE'),(52,'LUGGAGE','Radio krótkofalowe','Pozwala na kontakt ze światem zewnętrznym.',1,'LUGGAGE'),(53,'LUGGAGE','Apteczka','Podstawowe leki и materiały opatrunkowe.',1,'LUGGAGE'),(60,'CHARACTER','Optymista','Podnosi morale grupy w trudnych chwilach.',1,'CHARACTER'),(61,'CHARACTER','Agresywny','Często wywołuje konflikty, ale jest silny.',1,'CHARACTER'),(62,'CHARACTER','Empatyczny','Zawsze chętny do wysłuchania i pomocy.',1,'CHARACTER'),(63,'CHARACTER','Pracuś','Wykonuje zadania bez narzekania.',1,'CHARACTER'),(70,'SKILL','Mechanika','Potrafi naprawić generator i filtry powietrza.',1,'SKILL'),(71,'SKILL','Psychologia','Pomaga rozwiązywać spory wewnątrz bunkra.',1,'SKILL'),(72,'SKILL','Polowanie','Zna techniki tropienia i zdobywania mięsa.',1,'SKILL'),(73,'SKILL','Elektronika','Zna się na komputerach и obwodach.',1,'SKILL'),(80,'BUNKER','Biblioteka medyczna','Zawiera książki o chirurgii i chorobach.',1,'BUNKER'),(81,'BUNKER','Warsztat narzędziowy','Ułatwia wszelkie naprawy mechaniczne.',1,'BUNKER'),(82,'BUNKER','System filtracji wody','Pozwala na recykling wody w bunkrze.',1,'BUNKER'),(83,'BUNKER','Mała szklarnia','Możliwość uprawy warzyw pod ziemią.',1,'BUNKER'),(90,'CATASTROPHE','Epidemia zombie','Zagrożenie ze strony zainfekowanych istot.',1,'CATASTROPHE'),(91,'CATASTROPHE','Globalna powódź','Większość lądów znalazła się pod wodą.',1,'CATASTROPHE'),(92,'CATASTROPHE','Inwazja obcych','Technologicznie zaawansowana rasa atakuje Ziemię.',1,'CATASTROPHE'),(93,'CATASTROPHE','Nowa epoka lodowcowa','Ekstremalnie niskie temperatury na zewnątrz.',1,'CATASTROPHE');
/*!40000 ALTER TABLE `bunker_cards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_results`
--

DROP TABLE IF EXISTS `game_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_results` (
  `result_id` int NOT NULL AUTO_INCREMENT,
  `session_id` int NOT NULL,
  `winner_role_id` int NOT NULL,
  `winner_user_id` int DEFAULT NULL,
  `ended_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`result_id`),
  UNIQUE KEY `result_id` (`result_id`),
  KEY `FK_game_results_TO_users` (`winner_user_id`),
  KEY `FK_game_results_TO_roles` (`winner_role_id`),
  KEY `FK_game_results_TO_game_sessions` (`session_id`),
  CONSTRAINT `FK_game_results_TO_game_sessions` FOREIGN KEY (`session_id`) REFERENCES `game_sessions` (`session_id`) ON DELETE CASCADE,
  CONSTRAINT `FK_game_results_TO_roles` FOREIGN KEY (`winner_role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `FK_game_results_TO_users` FOREIGN KEY (`winner_user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_results`
--

LOCK TABLES `game_results` WRITE;
/*!40000 ALTER TABLE `game_results` DISABLE KEYS */;
INSERT INTO `game_results` VALUES (6,17,1,7,NULL),(7,17,1,9,NULL),(8,17,1,7,NULL),(9,17,1,8,NULL),(10,17,1,7,NULL),(11,17,1,10,NULL),(12,17,1,7,NULL),(13,17,1,9,NULL),(14,17,1,7,NULL),(15,17,1,10,NULL),(16,17,1,7,NULL),(17,17,1,10,NULL),(18,17,1,9,NULL),(19,17,1,10,NULL),(20,17,1,8,NULL),(21,17,1,9,NULL),(22,17,1,7,NULL),(23,17,1,9,NULL),(24,17,1,7,NULL),(25,17,1,9,NULL),(26,17,1,7,NULL),(27,17,1,9,NULL),(28,17,1,7,NULL),(29,17,1,9,NULL),(30,17,1,7,NULL),(31,17,1,9,NULL),(32,17,1,7,NULL),(33,17,1,10,NULL),(34,17,1,7,NULL),(35,17,1,9,NULL),(36,17,1,9,NULL),(37,17,1,10,NULL),(38,17,1,9,NULL),(39,17,1,10,NULL),(40,17,1,9,NULL),(41,17,1,10,NULL),(42,17,1,7,NULL),(43,17,1,9,NULL),(44,17,1,7,NULL),(45,17,1,8,NULL),(46,17,1,7,NULL),(47,17,1,9,NULL),(48,17,1,7,NULL),(49,17,1,8,NULL),(50,17,1,7,NULL),(51,17,1,8,NULL),(52,17,1,7,NULL),(53,17,1,8,NULL),(54,17,1,7,NULL),(55,17,1,10,NULL),(58,17,1,7,NULL),(59,17,1,8,NULL),(60,17,1,7,NULL),(61,17,1,10,NULL),(62,17,1,7,NULL),(63,17,1,9,NULL),(64,26,1,8,NULL),(65,26,1,9,NULL);
/*!40000 ALTER TABLE `game_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_rooms`
--

DROP TABLE IF EXISTS `game_rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_rooms` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `room_name` varchar(50) NOT NULL,
  `max_players` int DEFAULT '10',
  `created_by` int NOT NULL,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
  `is_active` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`room_id`),
  UNIQUE KEY `room_id` (`room_id`),
  UNIQUE KEY `room_name` (`room_name`),
  KEY `FK_game_rooms_TO_users` (`created_by`),
  CONSTRAINT `FK_game_rooms_TO_users` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_rooms`
--

LOCK TABLES `game_rooms` WRITE;
/*!40000 ALTER TABLE `game_rooms` DISABLE KEYS */;
INSERT INTO `game_rooms` VALUES (29,'robo1',6,7,'2026-03-31 15:16:45.611983',0),(31,'MyRoom',6,8,'2026-04-09 21:21:30.064721',0),(35,'123',5,7,'2026-05-14 15:45:10.519687',0),(36,'robo2',5,7,'2026-05-14 15:50:34.490783',0),(38,'robo3',5,8,'2026-05-14 15:51:44.530338',0),(39,'123456',5,7,'2026-05-14 22:50:38.035993',0);
/*!40000 ALTER TABLE `game_rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_sessions`
--

DROP TABLE IF EXISTS `game_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_sessions` (
  `session_id` int NOT NULL AUTO_INCREMENT,
  `room_id` int NOT NULL,
  `started_at` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
  `ended_at` datetime(6) DEFAULT NULL,
  `status` enum('WAITING','IN_PROGRESS','FINISHED') DEFAULT 'WAITING',
  `current_round` int DEFAULT '1',
  PRIMARY KEY (`session_id`),
  UNIQUE KEY `session_id` (`session_id`),
  KEY `FK_game_sessions_TO_game_rooms` (`room_id`),
  CONSTRAINT `FK_game_sessions_TO_game_rooms` FOREIGN KEY (`room_id`) REFERENCES `game_rooms` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_sessions`
--

LOCK TABLES `game_sessions` WRITE;
/*!40000 ALTER TABLE `game_sessions` DISABLE KEYS */;
INSERT INTO `game_sessions` VALUES (17,29,'2026-03-31 15:16:45.673677',NULL,'FINISHED',1),(19,31,'2026-04-09 21:21:30.095662',NULL,'IN_PROGRESS',1),(23,35,'2026-05-14 15:45:10.572016',NULL,'IN_PROGRESS',1),(24,36,'2026-05-14 15:50:34.497260',NULL,'WAITING',1),(25,38,'2026-05-14 15:51:44.540005',NULL,'IN_PROGRESS',1),(26,39,'2026-05-14 22:50:38.105281',NULL,'WAITING',1);
/*!40000 ALTER TABLE `game_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_cards`
--

DROP TABLE IF EXISTS `player_cards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player_cards` (
  `session_id` int NOT NULL,
  `user_id` int NOT NULL,
  `bunker_card_id` int NOT NULL,
  `revealed` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`session_id`,`user_id`,`bunker_card_id`),
  KEY `FK_player_cards_TO_users` (`user_id`),
  KEY `FK_player_cards_TO_bunker_cards` (`bunker_card_id`),
  CONSTRAINT `FK_player_cards_TO_bunker_cards` FOREIGN KEY (`bunker_card_id`) REFERENCES `bunker_cards` (`card_id`),
  CONSTRAINT `FK_player_cards_TO_game_sessions` FOREIGN KEY (`session_id`) REFERENCES `game_sessions` (`session_id`),
  CONSTRAINT `FK_player_cards_TO_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_cards`
--

LOCK TABLES `player_cards` WRITE;
/*!40000 ALTER TABLE `player_cards` DISABLE KEYS */;
INSERT INTO `player_cards` VALUES (17,7,6,0),(17,7,8,0),(17,7,12,0),(17,7,23,0),(17,7,30,0),(17,7,43,0),(17,7,50,0),(17,7,71,0),(17,7,93,0),(17,8,2,0),(17,8,3,0),(17,8,8,0),(17,8,13,0),(17,8,41,0),(17,8,51,0),(17,8,62,0),(17,8,73,0),(17,8,93,0),(17,9,1,0),(17,9,7,0),(17,9,8,0),(17,9,22,0),(17,9,31,1),(17,9,40,0),(17,9,53,0),(17,9,63,0),(17,9,93,0),(19,7,1,0),(19,7,4,0),(19,7,22,1),(19,7,32,0),(19,7,50,1),(19,7,62,0),(19,7,70,0),(19,7,82,0),(19,7,93,0),(19,8,7,0),(19,8,13,0),(19,8,21,0),(19,8,33,0),(19,8,41,1),(19,8,52,0),(19,8,61,0),(19,8,82,0),(19,8,93,0),(23,7,5,0),(23,7,11,0),(23,7,23,0),(23,7,30,1),(23,7,42,0),(23,7,60,0),(23,7,72,0),(23,7,81,0),(23,7,93,0),(23,8,1,0),(23,8,20,0),(23,8,32,0),(23,8,41,1),(23,8,52,0),(23,8,63,0),(23,8,70,0),(23,8,81,0),(23,8,93,0),(23,9,3,0),(23,9,4,0),(23,9,7,0),(23,9,13,1),(23,9,21,0),(23,9,50,0),(23,9,61,0),(23,9,81,0),(23,9,93,0),(25,7,3,0),(25,7,5,0),(25,7,8,0),(25,7,11,0),(25,7,20,1),(25,7,42,0),(25,7,61,0),(25,7,72,0),(25,7,93,0),(25,8,6,0),(25,8,8,0),(25,8,10,0),(25,8,23,0),(25,8,31,0),(25,8,40,0),(25,8,52,0),(25,8,71,0),(25,8,93,0),(25,9,4,0),(25,9,8,0),(25,9,13,0),(25,9,21,0),(25,9,32,0),(25,9,50,0),(25,9,62,0),(25,9,73,0),(25,9,93,0);
/*!40000 ALTER TABLE `player_cards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_roles`
--

DROP TABLE IF EXISTS `player_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player_roles` (
  `session_id` int NOT NULL,
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  `assigned_at` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`session_id`,`user_id`),
  KEY `FK_player_roles_TO_users` (`user_id`),
  KEY `FK_player_roles_TO_roles` (`role_id`),
  CONSTRAINT `FK_player_roles_TO_game_sessions` FOREIGN KEY (`session_id`) REFERENCES `game_sessions` (`session_id`),
  CONSTRAINT `FK_player_roles_TO_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `FK_player_roles_TO_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_roles`
--

LOCK TABLES `player_roles` WRITE;
/*!40000 ALTER TABLE `player_roles` DISABLE KEYS */;
INSERT INTO `player_roles` VALUES (17,7,1,'2026-05-14 15:48:59.412802'),(17,8,1,'2026-05-14 15:48:59.414188'),(17,9,1,'2026-05-14 15:48:59.415392'),(19,7,1,'2026-04-09 21:21:52.181536'),(19,8,1,'2026-04-09 21:21:52.182927'),(23,7,1,'2026-05-14 15:47:46.024739'),(23,8,1,'2026-05-14 15:47:46.026182'),(23,9,1,'2026-05-14 15:47:46.027967'),(25,7,1,'2026-05-14 16:02:39.044344'),(25,8,1,'2026-05-14 16:02:39.047826'),(25,9,1,'2026-05-14 16:02:39.050032');
/*!40000 ALTER TABLE `player_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  `description` text,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Survivor','Zwykły uczestnik, próbuje przetrwać.'),(2,'Leader','Ma zdolność decydowania w krytycznych momentach.'),(3,'Doctor','Może leczyć innych graczy.');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_players`
--

DROP TABLE IF EXISTS `room_players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_players` (
  `room_id` int NOT NULL,
  `user_id` int NOT NULL,
  `joined_at` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
  `is_ready` tinyint(1) DEFAULT '0',
  `role_in_room` enum('MODERATOR','PLAYER') DEFAULT 'PLAYER',
  `is_dead` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`room_id`,`user_id`),
  KEY `FK_room_players_TO_users` (`user_id`),
  CONSTRAINT `FK_room_players_TO_game_rooms` FOREIGN KEY (`room_id`) REFERENCES `game_rooms` (`room_id`),
  CONSTRAINT `FK_room_players_TO_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_players`
--

LOCK TABLES `room_players` WRITE;
/*!40000 ALTER TABLE `room_players` DISABLE KEYS */;
INSERT INTO `room_players` VALUES (29,7,'2026-03-31 15:16:45.668491',1,'MODERATOR',0),(31,7,'2026-04-09 21:21:41.451245',1,'PLAYER',0),(31,8,'2026-04-09 21:21:30.091742',1,'MODERATOR',0),(35,7,'2026-05-14 15:45:10.567180',1,'MODERATOR',0),(35,8,'2026-05-14 15:47:33.319107',1,'PLAYER',0),(35,9,'2026-05-14 15:47:38.468872',1,'PLAYER',0),(36,7,'2026-05-14 15:50:34.495589',1,'MODERATOR',0),(38,7,'2026-05-14 16:02:29.199583',1,'PLAYER',0),(38,8,'2026-05-14 15:51:44.538682',1,'MODERATOR',0),(38,9,'2026-05-14 16:02:33.704483',1,'PLAYER',0),(39,7,'2026-05-14 22:50:38.100093',0,'MODERATOR',0);
/*!40000 ALTER TABLE `room_players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `registration_date` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
  `is_active` tinyint(1) DEFAULT '0',
  `global_role` enum('ADMIN','PLAYER') DEFAULT 'PLAYER',
  `profile_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (7,'player1','player1@gmail.com','$2a$10$fI6.yc5SWpxbmtdgvTpOieSEvAJqipZ1NQe.zo64WRRcMVeMxloXS','2026-03-31 15:15:33.679020',0,'PLAYER',NULL),(8,'player2','player2@gmail.com','$2a$10$7oAXVQnLMhOX88IxUvh2RemdrC1Ql2nwuYNgRTGEPlQMViFJuSaOa','2026-03-31 15:16:02.016127',0,'PLAYER',NULL),(9,'player3','player3@gmail.com','$2a$10$dOM2YEeVDJjrJ82ui.vaMe8/VVw5hW4exUomCwu9CqvlxbwAO5U06','2026-03-31 15:16:28.845155',0,'PLAYER',NULL),(10,'player4','player4@gmail.com','$2a$10$vha9lpI1Foxh82TuFknZrOnZdWBuM8axnI4FHjD4K.SoAdpE2UxQe','2026-03-31 16:17:39.813327',0,'PLAYER',NULL),(14,'123','123@gmail.com','$2a$10$bX3ZYqCoRsPCqDzLrftI2eer5eoMTnguKxSKUnAF3p7TT8gIYRJiG','2026-04-10 20:22:47.202278',0,'PLAYER',NULL),(19,'player16','pl@gmail.com','$2a$10$ewenFAGgcUCvlgBlyCxLa.BOhEHSbmlzNkZLGGltf9ZaWm4FO938m','2026-04-10 20:44:56.820382',0,'PLAYER',NULL),(26,'Valik','val@bk.ru','$2a$10$Q5yOxy/ye/gfJDOD4PvujeD6NOFJobi/2FTKa.ZASy21oCWmGBXB.','2026-05-01 19:24:21.484555',0,'PLAYER',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `votes`
--

DROP TABLE IF EXISTS `votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `votes` (
  `vote_id` int NOT NULL AUTO_INCREMENT,
  `session_id` int NOT NULL,
  `voter_id` int NOT NULL,
  `target_id` int NOT NULL,
  `round_num` int NOT NULL,
  `created_at` datetime(6) DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`vote_id`),
  UNIQUE KEY `vote_id` (`vote_id`),
  UNIQUE KEY `UQ_vote_once` (`voter_id`,`session_id`,`round_num`),
  KEY `FK_votes_TO_game_sessions` (`session_id`),
  KEY `FK_votes_TO_target` (`target_id`),
  CONSTRAINT `FK_votes_TO_game_sessions` FOREIGN KEY (`session_id`) REFERENCES `game_sessions` (`session_id`),
  CONSTRAINT `FK_votes_TO_target` FOREIGN KEY (`target_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_votes_TO_users` FOREIGN KEY (`voter_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=516 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `votes`
--

LOCK TABLES `votes` WRITE;
/*!40000 ALTER TABLE `votes` DISABLE KEYS */;
INSERT INTO `votes` VALUES (510,17,7,8,1,'2026-05-14 15:49:55.131601'),(511,17,8,7,1,'2026-05-14 15:49:59.248677'),(512,17,9,8,1,'2026-05-14 15:50:01.745497');
/*!40000 ALTER TABLE `votes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'gameplatformnew'
--

--
-- Dumping routines for database 'gameplatformnew'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-15 13:39:19
