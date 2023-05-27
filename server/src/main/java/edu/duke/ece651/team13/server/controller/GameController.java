package edu.duke.ece651.team13.server.controller;

import edu.duke.ece651.team13.server.dto.GameDTO;
import edu.duke.ece651.team13.server.dto.GameDetailDTO;
import edu.duke.ece651.team13.server.dto.GamesDTO;
import edu.duke.ece651.team13.server.dto.OrdersDTO;
import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.UserEntity;
import edu.duke.ece651.team13.server.service.GameService;
import edu.duke.ece651.team13.server.service.OrderService;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * This is the controller for game functions
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    /**
     * Creates a new game with the specified number of players.
     * @param noOfPlayer an Integer representing the number of players for the game
     * @return a ResponseEntity object with a GameDTO object in the body if the game creation is successful
     * @throws ResponseStatusException with an HTTP status of BAD_REQUEST if the number of players is invalid
     * @throws ResponseStatusException with an HTTP status of NOT_FOUND if the game creation fails
     */
    @PostMapping("/createGame/{noOfPlayer}")
    public ResponseEntity<GameDTO> createGame(@PathVariable("noOfPlayer") Integer noOfPlayer) {
        log.info("Received an /createGame");
        try {
            if (noOfPlayer != 2 && noOfPlayer != 3 && noOfPlayer != 4) {
                throw new IllegalArgumentException("No of players should be either 2, 3, 4");
            }
            GameEntity gameEntity = gameService.createGame(noOfPlayer);
            return ResponseEntity.ok().body(new GameDTO(gameEntity.getId(),gameEntity.getPlayers().size()));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Retrieves a list of available free games for the specified user ID.
     * @param userId a Long representing the ID of the user
     * @return a ResponseEntity object with a GamesDTO object in the body if the retrieval is successful
     * @throws ResponseStatusException with an HTTP status of NOT_FOUND if the retrieval fails
     */
    @GetMapping("/getFreeGames")
    public ResponseEntity<GamesDTO> getAvailableFreeGames(@RequestParam("userId") Long userId) {
        log.info("Received a request /getFreeGames ");
        try {
            List<GameDTO> games = gameService.getFreeGames(userId);
            return ResponseEntity.ok().body(new GamesDTO(games));
        }  catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Retrieves a list of games linked to the specified user ID.
     * @param userId a Long representing the ID of the user
     * @return a ResponseEntity object with a GamesDTO object in the body if the retrieval is successful
     * @throws ResponseStatusException with an HTTP status of NOT_FOUND if the retrieval fails
     */
    @GetMapping("/userGames")
    public ResponseEntity<GamesDTO> getGamesOfUser(@RequestParam("userId") Long userId) {
        log.info("Received a request /userGames ");
        try {
            List<GameDTO> games = gameService.getGamesLinkedToPlayer(userId);
            return ResponseEntity.ok().body(new GamesDTO(games));
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Adds the player with the specified user ID to the specified game ID.
     * @param gameId a Long representing the ID of the game to join
     * @param userId a Long representing the ID of the user to add as a player
     * @return a ResponseEntity object with a PlayerEntity object in the body if the addition is successful
     * @throws ResponseStatusException with an HTTP status of BAD_REQUEST if the request is invalid
     * @throws ResponseStatusException with an HTTP status of NOT_FOUND if the addition fails
     */
    @PostMapping("/joinGame/{gameId}")
    public ResponseEntity<PlayerEntity> joinGame(@PathVariable("gameId") Long gameId, @RequestParam("userId") Long userId) {
        log.info("Received a request /joinGame/{gameId} ");
        try {
            PlayerEntity player = gameService.joinGame(gameId, userId);
            return ResponseEntity.ok().body(player);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Retrieves the game object with the specified game ID.
     * @param gameId a Long representing the ID of the game to retrieve
     * @return a ResponseEntity object with a GameEntity object in the body if the retrieval is successful
     * @throws ResponseStatusException with an HTTP status of NOT_FOUND if the retrieval fails
     */
    @GetMapping("/getGame/{gameId}")
    public ResponseEntity<GameEntity> getMap(@PathVariable("gameId") Long gameId) {
        log.info("Received an /getGame/");
        try {
            GameEntity game = gameService.getGame(gameId);
            return ResponseEntity.ok().body(game);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Retrieves the details of a game for a particular user, including the game entity, player entity, and
     * whether the player has submitted orders in this round.
     * @param gameId the ID of the game to retrieve.
     * @param userId the ID of the user whose game details are being retrieved.
     * @return a ResponseEntity with a GameDetailDTO containing the game entity, player entity, and whether the player has
     * made any orders yet, if successful, or an error message if the game or user cannot be found.
     */
    @GetMapping("/getGameForUser/{gameId}")
    public ResponseEntity<GameDetailDTO> getMap(@PathVariable("gameId") Long gameId, @RequestParam("userId") Long userId) {
        log.info("Received an /getGame/ for a particular user");
        try {
            UserEntity user = userService.getUserById(userId);
            GameEntity game = gameService.getGame(gameId);
            PlayerEntity player = playerService.getPlayerByUserAndGame(user, game);
            boolean isPlayerDone = (orderService.getOrdersByPlayer(player).size() != 0);
            GameDetailDTO gameDetailDTO = new GameDetailDTO(game, player, isPlayerDone);
            return ResponseEntity.ok().body(gameDetailDTO);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint for submitting orders for a player in a game.
     * @param ordersDTO the DTO containing the orders to be submitted.
     * @param playerId the ID of the player submitting the orders.
     * @return a ResponseEntity with a success message if the orders were successfully submitted,
     * or an error message if the player or game does not exist, or the orders are invalid.
     */
    @PostMapping("/submitOrder")
    public ResponseEntity<String> submitOrder(@RequestBody OrdersDTO ordersDTO, @RequestParam("playerId") Long playerId) {
        log.info("Received an /submitOrder");
        log.info("Player Id" + playerId);
        try {
            orderService.validateAndAddOrders(ordersDTO, playerId);
            return ResponseEntity.ok().body("Submitted successful");
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(NOT_FOUND, e.getMessage());
        }
    }


}
