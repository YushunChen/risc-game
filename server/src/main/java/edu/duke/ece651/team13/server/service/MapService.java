package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.MapEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

import java.util.List;

public interface MapService {

    /**
     * Returns a MapEntity object representing the map with the given map ID.
     * @param mapId the ID of the map to retrieve
     * @return a MapEntity object representing the retrieved map
     * @throws IllegalArgumentException if no map with the given ID is found
     */
    MapEntity getMap(Long mapId);

    /**
     * Creates a new map for a game with the given game entity and list of players.
     * @param gameEntity the game entity for which the map is being created
     * @param players the list of players for the game
     * @return the created map entity
     */
    MapEntity createMap(GameEntity gameEntity, List<PlayerEntity> players);
}
