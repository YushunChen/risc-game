package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.MapEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;

import java.util.List;

public interface TerritoryService {

    /**
     * Creates and returns a new TerritoryEntity object with the specified name, MapEntity, PlayerEntity,
     * food production, and tech production values.
     *
     * @param name            the name of the new territory
     * @param map             the MapEntity the territory belongs to
     * @param player          the PlayerEntity that owns the territory
     * @param foodProduction  the amount of food produced by the territory per round
     * @param techProduction  the amount of tech produced by the territory per round
     * @return the newly created TerritoryEntity object
     */
    TerritoryEntity createTerritory(String name, MapEntity map, PlayerEntity player, int foodProduction, int techProduction);

    TerritoryEntity getTerritoriesById(Long Id);

    List<TerritoryEntity> getTerritoriesByPlayer(PlayerEntity player);

    TerritoryEntity updateTerritoryOwner(TerritoryEntity territory, PlayerEntity owner);

    void addNeighbour(TerritoryEntity territory1, TerritoryEntity territory2, Integer distance);

    TerritoryEntity updateTerritoryUnits(TerritoryEntity territory, List<UnitEntity> units);

    TerritoryEntity updateTerritoryRemainingCloak(TerritoryEntity territory, int remainingCloak);

}
