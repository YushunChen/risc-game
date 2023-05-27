package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Check if player's tech resource totals is enough for the tech research
 */
public class TechResearchTechResourceChecker extends RuleChecker {
    public TechResearchTechResourceChecker(RuleChecker next) {
        super(next);
    }

    // This map shows how much it cost to upgrade one step from the current level
    // key is current level, value is the tech resource cost
    static final Map<Integer, Integer> techResourceCostMap  = new HashMap<Integer, Integer>() {{
        put(1, 20);
        put(2, 40);
        put(3, 80);
        put(4, 160);
        put(5, 320);
    }};

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if(player.getTechResource() < getTechCost(player)){
            throw new IllegalArgumentException("Invalid technology research order: The player does not" +
                    "have sufficient technology resource for the research.");
        }
    }

    /**
     * Returns the technology cost for a player to upgrade to next tech level
     * based on their current techn level
     * @param player The player to calculate the technology cost for
     * @return The technology cost
     */
    public static int getTechCost(PlayerEntity player){
        return techResourceCostMap.get(player.getMaxTechLevel());
    }
}
