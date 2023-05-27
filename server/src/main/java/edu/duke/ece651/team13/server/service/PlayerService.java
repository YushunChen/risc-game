package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.UserEntity;
import edu.duke.ece651.team13.server.enums.PlayerStatusEnum;

import java.util.List;

public interface PlayerService {
    /**
     * Creates a new player with the given name and adds them to the given game.
     *
     * @param name the name of the new player
     * @param game the game the player will join
     * @return the newly created PlayerEntity
     * @throws IllegalArgumentException if the name is null or empty or if the game is null or already started
     */
    PlayerEntity createPlayer(String name, GameEntity game);

    PlayerEntity getPlayer(Long Id);

    List<PlayerEntity> getPlayersByUser(UserEntity user);

    PlayerEntity updatePlayerStatus(PlayerEntity player, PlayerStatusEnum status);

    PlayerEntity updatePlayerTechResource(PlayerEntity player, int techResource);

    PlayerEntity updatePlayerFoodResource(PlayerEntity player, int foodResource);

    PlayerEntity updatePlayerUser(PlayerEntity player, UserEntity userEntity);

    PlayerEntity updatePlayerMaxTechLevel(PlayerEntity player, int techLevel);

    PlayerEntity getPlayerByUserAndGame(UserEntity user, GameEntity game);

    PlayerEntity updatePlayerCloakResearched(PlayerEntity player);

}
