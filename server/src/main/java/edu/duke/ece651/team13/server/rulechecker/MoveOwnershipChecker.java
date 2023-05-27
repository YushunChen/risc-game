package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;

/**
 * Check if the source territory of the order has valid ownership
 */
public class MoveOwnershipChecker extends RuleChecker {

    public MoveOwnershipChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if (order.getUnitType().equals(UnitMappingEnum.SPY)) {
            // Spy units have special move rules: can move to enemy territories
            return;
        }
        if (order.getSource().getOwner() != player) {
            throw new IllegalArgumentException("Invalid move order: The source territory is not owned by you.");
        } else if (order.getDestination().getOwner() != player) {
            throw new IllegalArgumentException("Invalid move order: The destination territory is not owned by you.");
        }
    }
}
