package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;

/**
 * Check if the source territory's unit number is valid after executing the order
 */
public class MoveUnitNumChecker extends RuleChecker {

    public MoveUnitNumChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        int sourceUnitNum;
        if(order.getUnitType().equals(UnitMappingEnum.SPY)){
            sourceUnitNum = order.getSource().getSpyForPlayer(player).getUnitNum();
        }
        else{
            sourceUnitNum = order.getSource().getUnitForType( order.getUnitType()).getUnitNum();
        }
        int moveUnitNum = order.getUnitNum();
        if (sourceUnitNum < moveUnitNum) {
            throw new IllegalArgumentException("Invalid move order: Don't have sufficient unit number in the territory.");
        } else if (moveUnitNum < 0) {
            throw new IllegalArgumentException("Invalid move order: The unit number to move should be >= 0.");
        }
    }
}
