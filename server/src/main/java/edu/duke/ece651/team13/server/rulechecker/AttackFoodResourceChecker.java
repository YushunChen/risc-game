package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

/**
 * Check if the player's food resource totals is enough for the attack order
 */
public class AttackFoodResourceChecker extends RuleChecker {
    public AttackFoodResourceChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        int foodResource = player.getFoodResource();
        int cost = getFoodCost(order);
        if (foodResource < cost) {
            throw new IllegalArgumentException("Invalid attack order: Player doesn't have sufficient food resource.");
        }
    }

    /**
     * Calculates the total food cost for the given order based on the number of units ordered.
     * @param order the order entity for which to calculate the food cost
     * @return the total food cost for the given order
     */
    public static int getFoodCost(OrderEntity order) {
        return 10 * order.getUnitNum();
    }
}
