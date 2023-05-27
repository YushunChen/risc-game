package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.getNextLevel;

/**
 * Check if the player's max tech level is enough for the unit upgrade order
 */
public class UnitUpgradeTechLevelChecker extends RuleChecker {
    public UnitUpgradeTechLevelChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if(getNextLevel(order.getUnitType()).getLevel() > player.getMaxTechLevel()){
            throw new IllegalArgumentException("Invalid unit upgrade order: Player doesn't have sufficient tech level to upgrade.");
        }
    }

}
