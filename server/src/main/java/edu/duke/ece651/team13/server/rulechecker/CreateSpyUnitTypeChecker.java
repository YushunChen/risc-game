package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;

import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL0;

/**
 * Check that only level 1 units or higher can upgrade to spy units
 */
public class CreateSpyUnitTypeChecker extends RuleChecker {
    public CreateSpyUnitTypeChecker(RuleChecker next){super(next);}

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        UnitMappingEnum unitType = order.getUnitType();
        if(unitType.equals(LEVEL0)){
            throw new IllegalArgumentException("Invalid create spy order: basic units cannot upgrade to spy units.");
        }
    }
}
