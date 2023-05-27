package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;


/**
 * Check if the target territory of the order has valid ownership
 */
public class CloakOwnershipChecker extends RuleChecker {

    public CloakOwnershipChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if (order.getSource().getOwner() != player) {
            throw new IllegalArgumentException("Invalid cloak order: The target territory is not owned by you.");
        }
    }
}
