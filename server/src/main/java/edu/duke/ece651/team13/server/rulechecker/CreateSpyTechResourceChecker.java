package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

/**
 * Check if the player's tech resource totals is enough for creating spies
 */
public class CreateSpyTechResourceChecker extends RuleChecker {
    public CreateSpyTechResourceChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        int techResource = player.getTechResource();
        int cost = getTechCost(order);
        if (techResource < cost){
            throw new IllegalArgumentException("Invalid Create Spy order: Player doesn't have sufficient tech resource.");
        }
    }

    public static int getTechCost(OrderEntity order){
        return order.getUnitNum() * 100;
    }
}
