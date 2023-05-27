package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.SpyUnitEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;

public interface SpyUnitService {
    /**
     * Creates a new spy unit with the specified owner and assigns it to the given territory with the specified number of units.
     * @param territory The territory to assign the unit to
     * @param unitNum The number of units to assign to the new unit
     * @param player The owner of the new unit
     * @return The newly created spy unit entity
     */
    public SpyUnitEntity createSpyUnit(TerritoryEntity territory, int unitNum, PlayerEntity player);

    /**
     * Updates the number of units of the given spy unit entity
     * @param spy is the spy unit entity to modify
     * @param unitNum is the unit number to update to
     * @return the updated spy unit entity
     */
    SpyUnitEntity updateSpyUnit(SpyUnitEntity spy, int unitNum);
}
