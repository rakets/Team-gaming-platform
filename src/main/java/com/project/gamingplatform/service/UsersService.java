package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.*;
import com.project.gamingplatform.entity.*;
import com.project.gamingplatform.repository.BunkerCardsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.repository.UsersRepository;
import com.project.gamingplatform.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoomPlayersService roomPlayersService;
    private final BunkerCardsService bunkerCardsService;
    private final BunkerCardsRepository bunkerCardsRepository;
    private final RoomPlayersRepository roomPlayersRepository;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, RoomPlayersService roomPlayersService, BunkerCardsService bunkerCardsService, BunkerCardsRepository bunkerCardsRepository, RoomPlayersRepository roomPlayersRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.roomPlayersService = roomPlayersService;
        this.bunkerCardsService = bunkerCardsService;
        this.bunkerCardsRepository = bunkerCardsRepository;
        this.roomPlayersRepository = roomPlayersRepository;
    }

    public Users addNew(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedUser = usersRepository.save(users);
        return savedUser;
    }

    //    get current user from DB
    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Users users = getUsersByUsername(username).orElseThrow(() ->
                    new UsernameNotFoundException("User not found"));
            return users;
        }
        return null;
    }

    //для установки текущему пользователю данных и возврата в контроллер.
    public UsersDTO getCurrentUsersDtoRegardingCurrentRoom(GameRoomsDTO gameRoomsDTO) {
        Users currentUser = getCurrentUser();
        UsersDTO usersDTO = convertEntityUserToDto(currentUser);

        RoomPlayers roomPlayer = roomPlayersRepository.findByUser_UserIdAndRoom_RoomId(usersDTO.getUserId(), gameRoomsDTO.getRoomId());
//        boolean statusReady = roomPlayersService.isUserReadyInRoom(gameRoomsDTO.getRoomId(), usersDTO.getUserId());
//        isUserReadyInRoom(statusReady, usersDTO);
        isUserReadyInRoom(roomPlayer.getIsReady(), usersDTO);
        isUserDead(roomPlayer.getDead(), usersDTO);
        whoIsModerator(usersDTO, gameRoomsDTO);

        return usersDTO;
    }

    public UsersDTO findById(int userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        UsersDTO usersDTO = convertEntityUserToDto(user);
        return usersDTO;
    }

    public Optional<Users> getUsersByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public UsersDTO convertEntityUserToDto(Users user) {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setUserId(user.getUserId());
        usersDTO.setUsername(user.getUsername());
        usersDTO.setGlobalRole(user.getGlobalRole());
        return usersDTO;
    }

    public List<UsersDTO> findAllUsersByGameRoom(GameRoomsDTO gameRoomsDTO) {
        List<RoomPlayers> roomPlayersList = roomPlayersService.findAllRoomPlayersByRoomId(gameRoomsDTO.getRoomId());
        List<UsersDTO> usersDTOList = new ArrayList<>();
        for (RoomPlayers roomPlayers : roomPlayersList) {
            UsersDTO usersDTO = convertEntityUserToDto(roomPlayers.getUser());

            whoIsModerator(usersDTO, gameRoomsDTO);
            isUserReadyInRoom(roomPlayers.getIsReady(), usersDTO);
            isUserDead(roomPlayers.getDead(), usersDTO);

            usersDTOList.add(usersDTO);
        }
        System.out.println("uzery: " + usersDTOList);
        return usersDTOList;
    }

    private void isUserReadyInRoom(boolean statusReady, UsersDTO usersDTO) {
        if (statusReady) {
            usersDTO.setReadyStatus(ReadyStatus.READY);
        }
    }

    private void isUserDead(boolean statusDead, UsersDTO user) {
        if (statusDead) {
            user.setDeadStatus(DeadStatus.DEAD);
        }
    }

    private void whoIsModerator(UsersDTO usersDTO, GameRoomsDTO gameRoomsDTO) {
        if (gameRoomsDTO.getCreatedBy().equals(usersDTO.getUserId())) {
            usersDTO.setGameRole(RoleInRoom.MODERATOR);
        }
    }

    //    //получение всех игроков комнаты вместе с их карточками
//    public List<UsersDTO> preparePlayersWithCards(GameRoomsDTO gameRoomsDTO, int currentUserId) {
//        List<UsersDTO> usersDTOList = findAllUsersByGameRoom(gameRoomsDTO);
//        Map<Integer, BunkerCardList> allCards = bunkerCardsService.getAllPlayersBunkerCardsDTOInRoom(gameRoomsDTO.getRoomId(), currentUserId);
//        for (UsersDTO user : usersDTOList) {
//            user.setBunkerCards(allCards.get(user.getUserId()));
//        }
//        return usersDTOList;
//    }

    //getting all players in room with their revealed cards
    public List<UsersDTO> preparePlayersWithRevealedCards(GameRoomsDTO gameRoomsDTO) {
        List<UsersDTO> usersDTOList = findAllUsersByGameRoom(gameRoomsDTO);
        Map<Integer, BunkerCardList> allCards = bunkerCardsService.getAllPlayersRevealedBunkerCardsInRoom(gameRoomsDTO.getRoomId());
        for (UsersDTO user : usersDTOList) {
            user.setRevealedBunkerCards(allCards.get(user.getUserId()));
        }
        return usersDTOList;
    }

    //getting current user with his cards
    public UsersDTO prepareCurrentPlayerWithCards(GameRoomsDTO gameRoomsDTO) {
        UsersDTO currentUser = getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);
        // revealed game cards
        List<BunkerCards> revealedBunkerCards = bunkerCardsRepository.getBunkerCardsByUserIdRoomIdRevealedStatus(currentUser.getUserId(), gameRoomsDTO.getRoomId(), true);
        BunkerCardList revealCards = bunkerCardsService.getBunkerCardsDTOByUserIdRoomId(revealedBunkerCards);
        currentUser.setRevealedBunkerCards(revealCards);
        // game info cards
        GameSessionInfo gameSessionInfo = bunkerCardsService.getGameSessionInfoCards(revealedBunkerCards);
        currentUser.setGameSessionInfo(gameSessionInfo);
        // unrevealed game cards
        List<BunkerCards> unrevealedBunkerCards = bunkerCardsRepository.getBunkerCardsByUserIdRoomIdRevealedStatus(currentUser.getUserId(), gameRoomsDTO.getRoomId(), false);
        BunkerCardList unrevealCards = bunkerCardsService.getBunkerCardsDTOByUserIdRoomId(unrevealedBunkerCards);
        currentUser.setUnrevealedBunkerCards(unrevealCards);
        return currentUser;
    }
}

