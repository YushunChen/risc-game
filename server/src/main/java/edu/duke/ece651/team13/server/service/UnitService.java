package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;

public interface UnitService {
    /**
     * Creates a new unit of the specified type and assigns it to the given territory with the specified number of units.
     * @param unitType The type of unit to create
     * @param entity The territory to assign the unit to
     * @param unitNum The number of units to assign to the new unit
     * @return The newly created unit entity
     */
    UnitEntity createUnit(UnitMappingEnum unitType, TerritoryEntity entity, int unitNum);

    /**
     * Updates the number of units in the given unit entity and returns the updated entity.
     * @param unit the unit entity to be updated
     * @param unitNum the new number of units for the entity
     * @return the updated unit entity
     */
    UnitEntity updateUnit(UnitEntity unit, int unitNum);
}
