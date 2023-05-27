package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;

import static edu.duke.ece651.team13.server.util.GraphUtil.findMinCost;
import static edu.duke.ece651.team13.server.util.GraphUtil.findMinCostForSpy;

public class MoveFoodResourceChecker extends RuleChecker {
    public MoveFoodResourceChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        int foodResource = player.getFoodResource();
        int cost = getFoodCost(order);
        if (foodResource < cost) {
            throw new IllegalArgumentException("Invalid move order: Player doesn't have sufficient food resource.");
        }
    }

    /**
     * Calculates the food cost of a given order.
     * @param order the order entity for which to calculate the food cost
     * @return the food cost of the given order
     */
    public static int getFoodCost(OrderEntity order) {
        if (order.getUnitType().equals(UnitMappingEnum.SPY)){
            return 2 * findMinCostForSpy(order.getSource(), order.getDestination(), order.getPlayer()) * order.getUnitNum();
        }
        return 2 * findMinCost(order.getSource(), order.getDestination()) * order.getUnitNum();
    }
}
