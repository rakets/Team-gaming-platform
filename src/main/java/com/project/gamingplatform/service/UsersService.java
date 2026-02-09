package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.dto.ReadyStatus;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.GameRooms;
import com.project.gamingplatform.entity.RoleInRoom;
import com.project.gamingplatform.entity.Users;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
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
    public UsersDTO getCurrentUsersDtoRegardingCurrentRoom(GameRoomsDTO gameRoomsDTO){
        Users currentUser = getCurrentUser();
        UsersDTO usersDTO = convertEntityUserToDto(currentUser);
        whoIsModerator(usersDTO, gameRoomsDTO);
        return  usersDTO;
    }

    public Optional<Users> getUsersByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public UsersDTO convertEntityUserToDto(Users user) {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setUserId(user.getUserId());
        usersDTO.setUsername(user.getUsername());
        usersDTO.setGlobalRole(user.getGlobalRole());
        usersDTO.setGameRole(RoleInRoom.PLAYER);
        usersDTO.setReadyStatus(ReadyStatus.UNREADY);
        return usersDTO;
    }

    public List<UsersDTO> findAllUsersByGameRoom(GameRoomsDTO gameRoomsDTO) {
        List<Users> usersList = usersRepository.findAllUsersByRoomId(gameRoomsDTO.getRoomId());
        List<UsersDTO> usersDTOList = new ArrayList<>();
        for (Users user : usersList) {
            UsersDTO usersDTO = convertEntityUserToDto(user);
            whoIsModerator(usersDTO, gameRoomsDTO);
            usersDTOList.add(usersDTO);
        }
//        whoIsModerator(usersDTOList, gameRoomsDTO.getCreatedBy());
        return usersDTOList;
    }

//    public void whoIsModerator(List<UsersDTO> usersDTOList, Integer idGameRoom){
//        for(UsersDTO usersDTO : usersDTOList){
//            if(idGameRoom.equals(usersDTO.getUserId())){
//                usersDTO.setGameRole(RoleInRoom.MODERATOR);
//            }
//        }
//    }

    public void whoIsModerator(UsersDTO usersDTO, GameRoomsDTO gameRoomsDTO) {
        if (gameRoomsDTO.getCreatedBy().equals(usersDTO.getUserId())) {
            usersDTO.setGameRole(RoleInRoom.MODERATOR);
        }
    }
}

