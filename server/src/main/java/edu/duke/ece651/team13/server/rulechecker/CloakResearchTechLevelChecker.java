package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

/**
 * Check if the player's max tech level is equal or larger than 3 for the cloak research order
 */
public class CloakResearchTechLevelChecker extends RuleChecker {
    public CloakResearchTechLevelChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if(player.getMaxTechLevel()<3){
            throw new IllegalArgumentException("Invalid cloak research order: Player's tech level is less than 3.");
        }
    }

}
