package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.AttackerEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;

import java.util.List;

public interface AttackerService {

    /**
     * Retrieves a list of attackers on a given territory.
     * @param territory the territory to check for attackers
     * @return a list of attackers on the given territory
     */
    List<AttackerEntity> getAttackers(TerritoryEntity territory);

    /**
     * Returns a list of all the attackers in the given territory.
     * @param territory The territory to check for attackers.
     * @return The list of all attackers in the territory.
     */
    AttackerEntity addAttacker(TerritoryEntity territory, PlayerEntity player, UnitMappingEnum unitType, Integer UnitNo);

    /**
     * Removes all attackers from the given territory.
     * @param territory the territory to clear attackers from
     */
    void clearAttackers(TerritoryEntity territory);
}
