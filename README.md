# Team gaming platform

---

**THIS IS NOT THE FINAL VERSION, THIS PROJECT IS IN PROGESS. THE DESCRIPTION WILL BE UPDATED DURING DURING PROGRESS.**

---

<p align="center">
    <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java" />
    <img src="https://img.shields.io/badge/Maven-4.0.0-C71A36?style=for-the-badge&logo=apachemaven" alt="Maven" />
    <img src="https://img.shields.io/badge/Spring_Boot-3.4.11-green?style=for-the-badge&logo=springboot" alt="Spring Boot 3" />
    <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security" />

    <img src="https://img.shields.io/badge/WebSocket-000000?style=for-the-badge&logo=socket.io&logoColor=white" alt="WebSocket" />
    <img src="https://img.shields.io/badge/LiveKit-0052FF?style=for-the-badge&logo=livekit&logoColor=white" alt="LiveKit" />


    <img src="https://img.shields.io/badge/MySQL-8-blue?style=for-the-badge&logo=mysql" alt="MySQL" />
    <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white" alt="Redis" />
    <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white" alt="Hibernate" />
    <img src="https://img.shields.io/badge/Jakarta%20Persistence-FF6600?style=for-the-badge&logo=jakartaee&logoColor=white" alt="JPA" />

    <img src="https://img.shields.io/badge/Thymeleaf-Template-darkgreen?style=for-the-badge&logo=thymeleaf" alt="Thymeleaf" />
    <img src="https://img.shields.io/badge/HTML-5-E34F26?style=for-the-badge&logo=html5" alt="HTML5" />
    <img src="https://img.shields.io/badge/CSS-3-1572B6?style=for-the-badge&logo=css3" alt="CSS3" />
    <img src="https://img.shields.io/badge/JavaScript-ES6-F7DF1E?style=for-the-badge&logo=javascript" alt="JavaScript" />
    <img src="https://img.shields.io/badge/Bootstrap-5.3.3-7952B3?style=for-the-badge&logo=bootstrap" alt="Bootstrap" />
</p>

---

**Team gaming platform** It's a web application for playing team games like “Bunker” in real time. Users can create their own game rooms and communicate by text, video, and voice chat. The app also includes tools to help with the game, such as role selection, game cards.

---

## 📑 Table of Contents
* [Tech Stack](#-tech-stack)
* [Screenshots](#-screenshots)
* [How to Run the Project](#-how-to-run-the-project)
* [Project Structure](#-project-structure)
* [Database Structure](#-database-structure)

---

## 🏗 Tech Stack

* **Java:** 21
* **Spring Boot:** 3.4.11
* **Spring Security, Hibernate, JPA**
* **Apache-Maven:** 4.0.0
* **Database:** MySQL, Redis
* **Frontend:** Thymeleaf, HTML5, CSS3, JavaScript, Bootstrap 
* **WebSocket, LiveKit**

---

## 📸 Screenshots

### 🏠 Welcome page

<p align="center">
  <img src="./docs/main_page.png" width="500"/>
</p>

---

### 👔 Login page

<p align="center">
  <img src="./docs/login_page.png" width="230"/>
</p>

---

### 👔 Register page

<p align="center">
  <img src="./docs/reg_page.png" width="600" hight="350"/>
</p>

---

### 👔 Dashboard page

<p align="center">
  <img src="./docs/dashboard_page.png" width="600" hight="350"/>
</p>
<p align="center">
  <img src="./docs/dashboard_page_2.png.png" width="600" hight="350"/>
</p>

---

### 👔 List of my game rooms

<p align="center">
  <img src="./docs/my_rooms_page.png" width="600" hight="350"/>
</p>

---

### 👔 Create new game room page

<p align="center">
  <img src="./docs/create_room_page.png" width="600" hight="350"/>
</p>

---

### 👔 Game lobby page

<p align="center">
  <img src="./docs/game_lobby_page.png" width="600" hight="350"/>
</p>

---

### 👔 Game session page

<p align="center">
  <img src="./docs/game_session_page1.png" width="600" hight="350"/>
</p>
<p align="center">
  <img src="./docs/game_session_page2.png" width="600" hight="350"/>
</p>

---

## 🚀 How to Run the Project

1.  The manual will be added in the future.

---

## 📂 Project Structure

```
gamingplatform/
│
├── src/
│   ├── main/
│   │   ├── java/com/gamingplatform/
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── CustomAuthenticationSuccessHandler.java
│   │   │   │   ├── RedisConfig.java
│   │   │   │   └── WebSecurityConfig.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── ChatController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── GameRoomsController.java
│   │   │   │   ├── GameSessionsController.java
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── LiveKitController.java
│   │   │   │   ├── RoomPlayersController.java
│   │   │   │   └── UsersController.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── BunkerCardList.java
│   │   │   │   ├── BunkerCardsDTO.java
│   │   │   │   ├── ChatMessageDTO.java
│   │   │   │   ├── DeadStatus.java
│   │   │   │   ├── GameRoomsDTO.java
│   │   │   │   ├── GameSessionInfo.java
│   │   │   │   ├── MessageType.java
│   │   │   │   ├── PlayerCardsDTO.java
│   │   │   │   ├── ReadyStatus.java
│   │   │   │   ├── RoomPlayersDTO.java
│   │   │   │   ├── ServerMessage.java
│   │   │   │   ├── UserActivityDTO.java
│   │   │   │   ├── UsersDTO.java
│   │   │   │   ├── VoteResult.java
│   │   │   │   └── VotesDTO.java
│   │   │   │
│   │   │   ├── entity/
│   │   │   │   ├── BunkerCards.java
│   │   │   │   ├── CardType.java
│   │   │   │   ├── GameResults.java
│   │   │   │   ├── GameRooms.java
│   │   │   │   ├── GameSessions.java
│   │   │   │   ├── GlobalRole.java
│   │   │   │   ├── PlayerCards.java
│   │   │   │   ├── PlayerCardsId.java
│   │   │   │   ├── PlayerRoles.java
│   │   │   │   ├── PlayerRolesId.java
│   │   │   │   ├── RoleInRoom.java
│   │   │   │   ├── Roles.java
│   │   │   │   ├── RoomPlayers.java
│   │   │   │   ├── RoomPlayersId.java
│   │   │   │   ├── SessionGameStatus.java
│   │   │   │   ├── Users.java
│   │   │   │   └── Votes.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── BunkerCardsRepository.java
│   │   │   │   ├── GameResultsRepository.java
│   │   │   │   ├── GameRoomsRepository.java
│   │   │   │   ├── GameSessionsRepository.java
│   │   │   │   ├── PlayerCardsRepository.java
│   │   │   │   ├── PlayerRolesRepository.java
│   │   │   │   ├── RolesRepository.java
│   │   │   │   ├── RoomPlayersRepository.java
│   │   │   │   ├── UsersRepository.java
│   │   │   │   └── VotesRepository.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── BunkerCardsService.java
│   │   │   │   ├── ChatService.java
│   │   │   │   ├── CustomUserDetailService.java
│   │   │   │   ├── GameProcessService.java
│   │   │   │   ├── GameResultsService.java
│   │   │   │   ├── GameRoomsService.java
│   │   │   │   ├── GameSessionsService.java
│   │   │   │   ├── LiveKitService.java
│   │   │   │   ├── RoomPlayersService.java
│   │   │   │   ├── UsersService.java
│   │   │   │   └── VotesService.java
│   │   │   │
│   │   │   ├── util/
│   │   │   │   └── CustomUserDetails.java
│   │   │   │
│   │   │   ├── websocket/
│   │   │   │   ├── WebSocketConfig.java
│   │   │   │   └── WebSocketService.java
│   │   │   │
│   │   │   └── GamingplatformApplication.java
│   │   │
│   │   └──  resources/
│   │        ├── static/
│   │        │   ├── assets
│   │        │   │   └── bunker-main.webp
│   │        │   ├── css
│   │        │   │   ├── bunker-main.webp
│   │        │   │   ├── bunker-main.webp
│   │        │   │   ├── bunker-main.webp
│   │        │   │   ├── bunker-main.webp
│   │        │   │   ├── bunker-main.webp
│   │        │   │   ├── bunker-main.webp
│   │        │   │   └── bunker-main.webp
│   │        │   │
│   │        │   ├── css
│   │        │   │   ├── font-awesome.min.css
│   │        │   │   ├── new-room.css
│   │        │   │   ├── style.css
│   │        │   │   ├── style-index.css
│   │        │   │   ├── style-login.css
│   │        │   │   └── style-reg.css
│   │        │   │
│   │        │   ├── fonts
│   │        │   │
│   │        │   ├── img
│   │        │   │   ├── camera.svg
│   │        │   │   └── mic.svg
│   │        │   │
│   │        │   └── templates
│   │        │       ├── createRoom.html
│   │        │       ├── dashboard.html
│   │        │       ├── gameRoom.html
│   │        │       ├── gameSession.html
│   │        │       ├── index.html
│   │        │       ├── login.html
│   │        │       ├── registerForm.html
│   │        │       └── roomsList.html
│   │        │
│   │        ├── templates/
│   │        │   ├── createRoom.html
│   │        │   ├── dashboard.html
│   │        │   ├── gameRoom.html
│   │        │   ├── gameSession.html
│   │        │   ├── index.html
│   │        │   ├── login.html
│   │        │   ├── registerForm.html
│   │        │   └── roomsList.html
│   │        │
│   │        ├──  application.properties
│   │        └──  log4j2-spring.xml
│   │
│   └── test/
│       └── java/com/sport_calendar/
│           └── GamingplatformApplicationTests.java
│
├── pom.xml
├── README.md
└── .gitignore
```

---

## 📂 Database structure

<p align="center">
  <a href="./docs/database-structure.png">
    <img src="./docs/database_diagram.png" width="600"/>
  </a>
</p>
