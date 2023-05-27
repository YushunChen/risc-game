package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.UserEntity;
import edu.duke.ece651.team13.server.enums.PlayerStatusEnum;
import edu.duke.ece651.team13.server.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.duke.ece651.team13.server.MockDataUtil.getGameEntity;
import static edu.duke.ece651.team13.server.MockDataUtil.getPlayerEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    private PlayerService service; //service under test

    @Mock
    private PlayerRepository repository;

    @BeforeEach
    void setUp() {
        service = new PlayerServiceImpl(repository);
    }

    @Test
    void getPlayerTest() {
        PlayerEntity player = getPlayerEntity();
        when(repository.findById(1L)).thenReturn(Optional.of(player));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        PlayerEntity actual = service.getPlayer(1L);
        assertEquals(player, actual);
        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository);

        assertThrows(NoSuchElementException.class, () -> service.getPlayer(2L));
    }

    @Test
    void getPlayerByUserTest() {
        UserEntity user = new UserEntity();
        List<PlayerEntity> players = new ArrayList<>();
        players.add(getPlayerEntity());
        when(repository.findByUser(user)).thenReturn(players);


        List<PlayerEntity> actual = service.getPlayersByUser(user);
        assertEquals(players.size(), actual.size());
        verify(repository, times(1)).findByUser(user);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void getPlayerByUserGameTest() {
        GameEntity game = getGameEntity();
        UserEntity user = new UserEntity();
        PlayerEntity player = getPlayerEntity();
        when(repository.findByUserAndGame(user, game)).thenReturn(Optional.of(player));


        PlayerEntity actual = service.getPlayerByUserAndGame(user, game);
        assertEquals(player, actual);
        verify(repository, times(1)).findByUserAndGame(user, game);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createPlayerTest() {
        GameEntity game = getGameEntity();
        PlayerEntity player = getPlayerEntity();
        when(repository.save(any(PlayerEntity.class))).thenReturn(player);
        PlayerEntity actual = service.createPlayer(player.getName(), game);
        assertEquals(player, actual);
        verify(repository, times(1)).save(any(PlayerEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updatePlayerStatusTest() {
        PlayerEntity player = getPlayerEntity();

        when(repository.save(any(PlayerEntity.class))).thenReturn(player);

        PlayerEntity actual = service.updatePlayerStatus(player, PlayerStatusEnum.LOSE);
        assertEquals(player, actual);

        verify(repository, times(1)).save(any(PlayerEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updatePlayerTechResourceTest() {
        PlayerEntity player = getPlayerEntity();

        when(repository.save(any(PlayerEntity.class))).thenReturn(player);

        PlayerEntity actual = service.updatePlayerTechResource(player, 2);
        assertEquals(player, actual);

        verify(repository, times(1)).save(any(PlayerEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updatePlayerFoodResourceTest() {
        PlayerEntity player = getPlayerEntity();

        when(repository.save(any(PlayerEntity.class))).thenReturn(player);

        PlayerEntity actual = service.updatePlayerFoodResource(player, 2);
        assertEquals(player, actual);

        verify(repository, times(1)).save(any(PlayerEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updatePlayerUserTest() {
        PlayerEntity player = getPlayerEntity();

        when(repository.save(any(PlayerEntity.class))).thenReturn(player);

        PlayerEntity actual = service.updatePlayerUser(player, new UserEntity());
        assertEquals(player, actual);

        verify(repository, times(1)).save(any(PlayerEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updatePlayerMaxTechLevelTest() {
        PlayerEntity player = getPlayerEntity();

        when(repository.save(any(PlayerEntity.class))).thenReturn(player);

        PlayerEntity actual = service.updatePlayerMaxTechLevel(player, 1);
        assertEquals(player, actual);

        verify(repository, times(1)).save(any(PlayerEntity.class));
        verifyNoMoreInteractions(repository);
    }
}
