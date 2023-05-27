package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.dto.GameDTO;
import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.UserEntity;
import edu.duke.ece651.team13.server.enums.GameStatusEnum;
import edu.duke.ece651.team13.server.repository.GameRepository;
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
import static edu.duke.ece651.team13.server.MockDataUtil.getUserEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    private GameService service; //service under test

    @Mock
    private GameRepository repository;

    @Mock
    private PlayerService playerService;

    @Mock
    private  UserService userService;

    @Mock
    private MapService mapService;

    @BeforeEach
    void setUp(){
        service = new GameServiceImpl(repository, playerService, userService, mapService);
    }

    @Test
    void getGameTest(){
        GameEntity game = getGameEntity();

        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->service.getGame(1L));

        when(repository.findById(1L)).thenReturn(Optional.of(game));
        GameEntity actual = service.getGame(1L);
        assertEquals(game, actual);
    }

    @Test
    void getFreeGamesTest(){
        UserEntity loggedInUser = new UserEntity();
        loggedInUser.setId(1L);
        GameEntity game = getGameEntity();
        PlayerEntity player = getPlayerEntity();
        player.setUser(new UserEntity());
        game.getPlayers().add(player);

        List<GameEntity> games = new ArrayList<>();
        games.add(game);

        when(repository.findByRoundNo(0)).thenReturn(games);
        when(userService.getUserById(loggedInUser.getId())).thenReturn(loggedInUser);

        List<GameDTO> freeGames = service.getFreeGames(loggedInUser.getId());
        assertEquals(0, freeGames.size());

        PlayerEntity player1 = getPlayerEntity();
        player1.setUser(null);
        game.getPlayers().add(player1);

        List<GameDTO> freeGames1 = service.getFreeGames(loggedInUser.getId());
        assertEquals(1, freeGames1.size());

        game.getPlayers().get(0).setUser(loggedInUser);
        List<GameDTO> freeGames2 = service.getFreeGames(loggedInUser.getId());
        assertEquals(0, freeGames2.size());
    }

    @Test
    void  getGamesLinkedToPlayeTest(){
        UserEntity loggedInUser = new UserEntity();
        loggedInUser.setId(1L);
        GameEntity game = getGameEntity();
        game.setId(1L);
        List<PlayerEntity> players = new ArrayList<>();
        PlayerEntity player = getPlayerEntity();
        player.setUser(new UserEntity());
        player.setGame(game);
        players.add(player);
        game.getPlayers().add(player);

        when(userService.getUserById(loggedInUser.getId())).thenReturn(loggedInUser);
        when(playerService.getPlayersByUser(any())).thenReturn(players);

        List<GameDTO> games = service.getGamesLinkedToPlayer(loggedInUser.getId());
        assertEquals(1, games.size());

    }

    @Test
    void createGamesTest(){
        GameEntity game = getGameEntity();
        when(repository.save(any(GameEntity.class))).thenReturn(game);

        when(playerService.createPlayer(any(), any())).thenReturn(new PlayerEntity());

        GameEntity actual = service.createGame(1);
        assertEquals(game, actual);
    }

    @Test
    void updateGameRoundAndStatusTest(){
        GameEntity game = getGameEntity();
        when(repository.save(any(GameEntity.class))).thenReturn(game);
        GameEntity actual = service.updateGameRoundAndStatus(game, GameStatusEnum.PLAYING, 1);
        assertEquals(game, actual);
    }

    @Test
    void joinGameTest(){
        PlayerEntity player = new PlayerEntity();
        GameEntity game = getGameEntity();
        game.getPlayers().add(player);
        UserEntity user = getUserEntity();
        user.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(game));
        when(userService.getUserById(1L)).thenReturn(user);
        when(userService.getUserById(2L)).thenReturn(new UserEntity());

        PlayerEntity actual = service.joinGame(1L, user.getId());
        assertEquals(player, actual);

        PlayerEntity player2 = new PlayerEntity();
        player2.setUser(user);
        game.getPlayers().add(player2);
        when(repository.findById(1L)).thenReturn(Optional.of(game));

        assertThrows(IllegalArgumentException.class, ()->service.joinGame(1L, user.getId()));

        player.setUser(user);
        when(repository.findById(1L)).thenReturn(Optional.of(game));
        assertThrows(NoSuchElementException.class, ()->service.joinGame(1L,2L));
    }
}

