package com.project.gamingplatform.service;

import com.project.gamingplatform.dto.DeadStatus;
import com.project.gamingplatform.dto.GameRoomsDTO;
import com.project.gamingplatform.dto.ReadyStatus;
import com.project.gamingplatform.dto.UsersDTO;
import com.project.gamingplatform.entity.GlobalRole;
import com.project.gamingplatform.entity.RoleInRoom;
import com.project.gamingplatform.entity.RoomPlayers;
import com.project.gamingplatform.entity.Users;
import com.project.gamingplatform.repository.BunkerCardsRepository;
import com.project.gamingplatform.repository.RoomPlayersRepository;
import com.project.gamingplatform.repository.UsersRepository;
import jakarta.inject.Inject;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoomPlayersService roomPlayersService;
    @Mock
    private BunkerCardsService bunkerCardsService;
    @Mock
    private BunkerCardsRepository bunkerCardsRepository;
    @Mock
    private RoomPlayersRepository roomPlayersRepository;

    @InjectMocks
    private UsersService usersService;

    private Users testUser;
    private Users testUser2;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        ReflectionTestUtils.setField(testUser, "userId", 1);
        testUser.setUsername("Player1");
        testUser.setPassword("simplePassword");
        testUser.setIsActive(true);
    }

    @Test
    void addNew_ShouldEncodePasswordAndSaveUser() {
        // GIVEN
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("simplePassword")).thenReturn(encodedPassword);
        when(usersRepository.save(any(Users.class))).thenReturn(testUser);
        // WHEN
        Users savedUser = usersService.addNew(testUser);
        // THEN
        assertNotNull(savedUser);
        assertEquals(encodedPassword, testUser.getPassword());
        verify(passwordEncoder, times(1)).encode("simplePassword");
        verify(usersRepository, times(1)).save(testUser);
    }

    @Test
    void getCurrentUser_ShouldReturnUser_WhenAuthenticated() {
        // GIVEN
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("Player1");
        SecurityContextHolder.setContext(securityContext);

        when(usersRepository.findByUsername("Player1")).thenReturn(Optional.of(testUser));
        // WHEN
        Users currentUser = usersService.getCurrentUser();
        // THEN
        assertNotNull(currentUser);
        assertEquals("Player1", currentUser.getUsername());
        verify(usersRepository, times(1)).findByUsername("Player1");

        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUsersDtoRegardingCurrentRoom_ShouldReturnPopulatedDTO() {
        // GIVEN
        // mock for security
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("Player1");
        SecurityContextHolder.setContext(securityContext);
        when(usersRepository.findByUsername("Player1")).thenReturn(Optional.of(testUser));

        // preparation room's dto
        GameRoomsDTO gameRoomsDTO = new GameRoomsDTO();
        gameRoomsDTO.setRoomId(18);
        gameRoomsDTO.setCreatedBy(1);

        // preparation player in the room
        RoomPlayers roomPlayers = new RoomPlayers();
        roomPlayers.setIsReady(true);
        roomPlayers.setDead(false);
        when(roomPlayersRepository.findByUser_UserIdAndRoom_RoomId(1, 18)).thenReturn(roomPlayers);

        // WHEN
        UsersDTO resultUsersDTO = usersService.getCurrentUsersDtoRegardingCurrentRoom(gameRoomsDTO);

        // THEN
        assertNotNull(resultUsersDTO);
        assertEquals("Player1", resultUsersDTO.getUsername());
        assertEquals(1, resultUsersDTO.getUserId());
        assertEquals(ReadyStatus.READY, resultUsersDTO.getReadyStatus());
        assertEquals(DeadStatus.ALIVE, resultUsersDTO.getDeadStatus());
        assertEquals(RoleInRoom.MODERATOR, resultUsersDTO.getGameRole());

        SecurityContextHolder.clearContext();
    }

    @Test
    void getUserDTORegardingUserId_ShouldReturnPopulatedDTO() {
        // GIVEN
        when(usersRepository.findById(1)).thenReturn(Optional.of(testUser));
        // WHEN
        UsersDTO resultUserDTO = usersService.findById(1);
        // THEN
        assertNotNull(resultUserDTO);
        assertEquals("Player1", resultUserDTO.getUsername());
        assertEquals(1, resultUserDTO.getUserId());
        assertEquals(GlobalRole.PLAYER, resultUserDTO.getGlobalRole());
    }

    @Test
    void getUserDTORegardingUser_ShouldReturnPopulatedDTO() {
        // WHEN
        UsersDTO resultUserDTO = usersService.convertEntityUserToDto(testUser);
        // THEN
        assertNotNull(resultUserDTO);
        assertEquals(1, resultUserDTO.getUserId());
        assertEquals("Player1", resultUserDTO.getUsername());
        assertEquals(GlobalRole.PLAYER, resultUserDTO.getGlobalRole());
    }

    @Test
    void getUserDTOListRegardingGameRoomsDTO_ShouldReturnPopulatedListOfUserDTO() {
        // GIVEN
        // preparation room's dto
        GameRoomsDTO gameRoomsDTO = new GameRoomsDTO();
        gameRoomsDTO.setCreatedBy(1);
        gameRoomsDTO.setRoomId(18);
        // preparation RoomPlayers1
        RoomPlayers roomPlayers1 = new RoomPlayers();
        roomPlayers1.setUser(testUser);
        roomPlayers1.setIsReady(true);
        roomPlayers1.setDead(false);

        // preparation testUser2 and RoomPlayers2
        testUser2 = new Users();
        ReflectionTestUtils.setField(testUser2, "userId", 2);
        testUser2.setUsername("Player2");
        testUser2.setPassword("simplePassword");
        testUser2.setIsActive(true);

        RoomPlayers roomPlayers2 = new RoomPlayers();
        roomPlayers2.setUser(testUser2);
        roomPlayers2.setIsReady(true);
        roomPlayers2.setDead(true);

        // preparation list of RoomPlayers
        List<RoomPlayers> roomPlayersList = Arrays.asList(roomPlayers1, roomPlayers2);

        when(roomPlayersService.findAllRoomPlayersByRoomId(18)).thenReturn(roomPlayersList);

        // WHEN
        List<UsersDTO> resultUsersDTOList = usersService.findAllUsersByGameRoom(gameRoomsDTO);

        // THEN
        assertNotNull(resultUsersDTOList);
        assertEquals(2, resultUsersDTOList.size());

        UsersDTO usersDTO1 = resultUsersDTOList.get(0);
        assertEquals(1, usersDTO1.getUserId());
        assertEquals("Player1", usersDTO1.getUsername());
        assertEquals(GlobalRole.PLAYER, usersDTO1.getGlobalRole());
        assertEquals(RoleInRoom.MODERATOR, usersDTO1.getGameRole());
        assertEquals(ReadyStatus.READY, usersDTO1.getReadyStatus());
        assertEquals(DeadStatus.ALIVE, usersDTO1.getDeadStatus());

        UsersDTO usersDTO2 = resultUsersDTOList.get(1);
        assertEquals(2, usersDTO2.getUserId());
        assertEquals("Player2", usersDTO2.getUsername());
        assertEquals(GlobalRole.PLAYER, usersDTO2.getGlobalRole());
        assertEquals(RoleInRoom.PLAYER, usersDTO2.getGameRole());
        assertEquals(ReadyStatus.READY, usersDTO2.getReadyStatus());
        assertEquals(DeadStatus.DEAD, usersDTO2.getDeadStatus());

        verify(roomPlayersService, times(1)).findAllRoomPlayersByRoomId(18);
    }
}
