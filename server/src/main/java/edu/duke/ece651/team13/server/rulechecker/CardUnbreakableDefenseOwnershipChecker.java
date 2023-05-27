package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

/**
 * Checks for correct ownership for the source and destination territories,
 * i.e., source territory is controlled by the player, and
 * destination territory is controlled by a different player
 */
public class CardUnbreakableDefenseOwnershipChecker extends RuleChecker {
    public CardUnbreakableDefenseOwnershipChecker(RuleChecker next) {
        super(next);
    }


    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if (order.getSource().getOwner() != player) {
            throw new IllegalArgumentException("Invalid attack order: The source territory is not owned by you.");
        }
    }
}
