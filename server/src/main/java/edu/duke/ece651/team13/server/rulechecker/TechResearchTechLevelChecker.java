package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

/**
 * Check if the player has already reached the max tech level allowed (6)
 */
public class TechResearchTechLevelChecker extends RuleChecker {
    public TechResearchTechLevelChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if (player.getMaxTechLevel() == 6) {
            throw new IllegalArgumentException("Invalid technology research order: The player has already reached" +
                    "the max technology level.");
        }
    }
}
