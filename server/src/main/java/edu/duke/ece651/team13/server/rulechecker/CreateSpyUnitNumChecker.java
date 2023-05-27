package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

/**
 * Check if the source territory's unit number is valid after executing the order
 */
public class CreateSpyUnitNumChecker extends RuleChecker {
    public CreateSpyUnitNumChecker(RuleChecker next){
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        int sourceUnitNum = order.getSource().getUnitForType( order.getUnitType()).getUnitNum();
        int unitNumToBecomeSpy = order.getUnitNum();
        if (sourceUnitNum < unitNumToBecomeSpy) {
            throw new IllegalArgumentException("Invalid create spy order: Don't have sufficient unit number in the territory.");
        } else if (unitNumToBecomeSpy < 0) {
            throw new IllegalArgumentException("Invalid create spy order: The unit number to move should be >= 0.");
        }
    }
}
