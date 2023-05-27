package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

/**
 * Check if the source territory's unit number is valid after executing the order
 */
public class UnitUpgradeUnitNumChecker extends RuleChecker {

    public UnitUpgradeUnitNumChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        int territoryUnitNum = order.getSource().getUnitForType(order.getUnitType()).getUnitNum();
        int upgradeUnitNum = order.getUnitNum();
        if (territoryUnitNum < upgradeUnitNum) {
            throw new IllegalArgumentException("Invalid unit upgrade order: Don't have sufficient unit number in the territory.");
        } else if (upgradeUnitNum < 0) {
            throw new IllegalArgumentException("Invalid unit upgrade order: The unit number to move should be >= 0.");
        }
    }
}
