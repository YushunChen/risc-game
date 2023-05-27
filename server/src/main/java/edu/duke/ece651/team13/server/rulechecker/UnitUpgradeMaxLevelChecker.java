package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;

import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL6;

/**
 * Check if the player's tech resource totals is enough for the unit upgrade
 */
public class UnitUpgradeMaxLevelChecker extends RuleChecker {
    public UnitUpgradeMaxLevelChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        UnitMappingEnum unitType = order.getUnitType();
        if (unitType.equals(LEVEL6)) {
            throw new IllegalArgumentException("Invalid upgrade order: You cannot further update Level 6 units.");
        }
    }
}

