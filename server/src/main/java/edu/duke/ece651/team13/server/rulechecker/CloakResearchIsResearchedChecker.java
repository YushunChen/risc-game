package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;


/**
 * Check if the player has researched the cloak
 */
public class CloakResearchIsResearchedChecker extends RuleChecker {
    public CloakResearchIsResearchedChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if(player.isCloakResearched()){
            throw new IllegalArgumentException("Invalid cloak research order: Player has researched the cloak.");
        }
    }

}
