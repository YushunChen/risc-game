package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

/**
 * Checks for correct ownership for the source and destination territories,
 * i.e., source territory is controlled by the player, and
 * destination territory is null as Unit upgrade doesn't depend on destination
 */
public class UnitUpgradeOwnershipChecker extends RuleChecker {
    public UnitUpgradeOwnershipChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if (order.getSource().getOwner() != player) {
            throw new IllegalArgumentException("Invalid unit upgrade order: The source territory is not owned by you.");
        }
        if (order.getDestination() != null) {
            throw new IllegalArgumentException("Invalid unit upgrade order: The destination territory should be null for unit upgrade.");
        }

    }
}
