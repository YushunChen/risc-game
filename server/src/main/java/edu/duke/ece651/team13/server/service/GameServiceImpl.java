package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.dto.GameDTO;
import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.UserEntity;
import edu.duke.ece651.team13.server.enums.GameStatusEnum;
import edu.duke.ece651.team13.server.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    @Autowired
    private final GameRepository repository;

    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final MapService mapService;

    private final List<String> playersName = Arrays.asList("Red", "Blue", "Green", "Yellow");

    @Override
    public GameEntity getGame(Long gameId) {
        Optional<GameEntity> game = repository.findById(gameId);
        if (game.isPresent()) {
            return game.get();
        } else {
            log.error("Did not find Game with Id " + gameId);
            throw new NoSuchElementException("Game with Id " + gameId + " does not exists");
        }
    }

    @Override
    public List<GameDTO> getFreeGames(Long userId) {
        List<GameEntity> games = repository.findByRoundNo(0);
        UserEntity loggedInUser = userService.getUserById(userId);
        return games.stream()
                .filter(game -> game.getPlayers().stream().noneMatch(u-> loggedInUser.equals(u.getUser())))
                .filter(game -> game.getPlayers().stream()
                        .anyMatch(player -> player.getUser() == null))
                .map(game -> new GameDTO(game.getId(), game.getPlayers().size()))
                .collect(Collectors.toList());
    }

    @Override
    public PlayerEntity joinGame(Long gameId, Long userId) {
        GameEntity game = getGame(gameId);
        UserEntity loggedInUser = userService.getUserById(userId);

        if(game.getPlayers().stream().anyMatch(player -> loggedInUser.equals(player.getUser()))){
            throw new IllegalArgumentException("User has already joined this game.");
        }

        for (PlayerEntity playerEntity : game.getPlayers()) {
            if (playerEntity.getUser() == null) {
                playerService.updatePlayerUser(playerEntity, loggedInUser);
                return playerEntity;
            }
        }

        throw new NoSuchElementException("Game does not have any free players");
    }

    @Override
    public List<GameDTO> getGamesLinkedToPlayer(Long userId) {
        UserEntity user = userService.getUserById(userId);
        List<PlayerEntity> players = playerService.getPlayersByUser(user);
        return players.stream().map(PlayerEntity::getGame)
                .map(game -> new GameDTO(game.getId(), game.getPlayers().size()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GameEntity createGame(int no_players) {
        GameEntity gameEntity = repository.save(new GameEntity());
        List<PlayerEntity> players = new ArrayList<>();
        for (int i = 0; i < no_players; i++) {
            players.add(playerService.createPlayer(playersName.get(i), gameEntity));
        }

        mapService.createMap(gameEntity, players);
        return gameEntity;
    }

    @Override
    @Transactional
    public GameEntity updateGameRoundAndStatus(GameEntity game, GameStatusEnum status, int roundNo) {
        game.setStatus(status);
        game.setRoundNo(roundNo);
        return repository.save(game);
    }

}
