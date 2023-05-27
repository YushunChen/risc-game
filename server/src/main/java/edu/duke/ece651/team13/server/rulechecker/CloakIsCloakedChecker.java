package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;


/**
 * Check if the territory is cloaked
 */
public class CloakIsCloakedChecker extends RuleChecker {
    public CloakIsCloakedChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if(order.getSource().getRemainingCloak()>0){
            throw new IllegalArgumentException("Invalid cloak order: The territory is cloaked.");
        }
    }

}
