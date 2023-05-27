package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.dto.GameDTO;
import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.enums.GameStatusEnum;

import java.util.List;

public interface GameService {

    /**
     * Retrieves a list of free games available for a given user.
     * @param userId the ID of the user
     * @return a list of {@link GameDTO} representing free games that the user can join
     */
    List<GameDTO> getFreeGames(Long userId);

    /**
     * Attempts to join a user to a game, returning the player that was added to the game.
     * @param gameId the ID of the game
     * @param userId the ID of the user
     * @return the {@link PlayerEntity} that was added to the game
     */
    PlayerEntity joinGame(Long gameId, Long userId);

    /**
     * Retrieves a list of games that a given user is linked to.
     * @param userId the ID of the user
     * @return a list of {@link GameDTO} representing the games that the user is linked to
     */
    List<GameDTO> getGamesLinkedToPlayer(Long userId);

    /**
     * Creates a new game with the given number of units.
     * @param unitNo the number of units to be created in the game
     */
    GameEntity createGame(int unitNo);

    /**
     * Returns the GameEntity with the specified game ID.
     * @param gameId the ID of the game to retrieve
     * @return the GameEntity with the specified ID
     */
    GameEntity getGame(Long gameId);

    /**
     * Updates the round number and status of the given game entity and returns the updated entity.
     * @param game The game entity to update.
     * @param status The new status to set for the game.
     * @param roundNo The new round number to set for the game.
     * @return The updated game entity.
     */
    GameEntity updateGameRoundAndStatus(GameEntity game, GameStatusEnum status, int roundNo);
}
