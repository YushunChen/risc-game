package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.TerritoryEntity;

public interface CombatResolutionService {

    /**
     * Resolves a combat in the given territory by comparing the attackers and defenders.
     * @param territory the territory where the combat takes place
     */
    void resolveCombot(TerritoryEntity territory);

}
