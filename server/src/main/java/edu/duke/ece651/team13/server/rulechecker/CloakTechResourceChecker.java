package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;


/**
 * Check if the player's tech resource totals is enough for the cloak order
 */
public class CloakTechResourceChecker extends RuleChecker {
    public CloakTechResourceChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        int techResource = player.getTechResource();
        int cost = getTechCost();
        if (techResource < cost) {
            throw new IllegalArgumentException("Invalid cloak order: Player doesn't have sufficient tech resource.");
        }
    }

    public static int getTechCost() {
        return 100;
    }
}
